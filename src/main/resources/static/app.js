const API = '/api/projetos';
const list = document.querySelector('#project-list');
const emptyState = document.querySelector('#empty-state');
const dialog = document.querySelector('#project-dialog');
const form = document.querySelector('#project-form');
const formError = document.querySelector('#form-error');
let projects = [];
let toastTimer;

const dateFormat = new Intl.DateTimeFormat('pt-BR', { dateStyle: 'medium', timeZone: 'UTC' });
const toDate = (value) => value ? dateFormat.format(new Date(`${value}T00:00:00Z`)) : '—';
const initials = (name = '') => name.trim().split(/\s+/).slice(0, 2).map(part => part[0]).join('').toUpperCase();

function showToast(message) {
  const toast = document.querySelector('#toast');
  toast.textContent = message;
  toast.classList.add('visible');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove('visible'), 3200);
}

function statusFor(project) {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const start = new Date(`${project.dataInicio}T00:00:00`);
  const end = new Date(`${project.dataFim}T23:59:59`);
  if (end < today) return { label: 'Concluído', className: 'done' };
  if (start <= today) return { label: 'Em andamento', className: 'active' };
  return { label: 'Planejado', className: 'planned' };
}

function render() {
  const query = document.querySelector('#search-input').value.trim().toLocaleLowerCase('pt-BR');
  const filtered = projects.filter(project => project.descricao.toLocaleLowerCase('pt-BR').includes(query));
  document.querySelector('#total-count').textContent = projects.length;
  document.querySelector('#nav-count').textContent = projects.length;
  document.querySelector('#active-count').textContent = projects.filter(project => statusFor(project).className === 'active').length;
  const next30 = new Date();
  next30.setHours(23, 59, 59, 999);
  next30.setDate(next30.getDate() + 30);
  document.querySelector('#upcoming-count').textContent = projects.filter(project => {
    const end = new Date(`${project.dataFim}T00:00:00`);
    return end >= new Date() && end <= next30;
  }).length;
  document.querySelector('#result-count').textContent = `${filtered.length} ${filtered.length === 1 ? 'projeto' : 'projetos'}`;
  list.replaceChildren();
  emptyState.classList.toggle('visible', filtered.length === 0);
  if (!filtered.length) {
    emptyState.querySelector('h3').textContent = projects.length && query ? 'Nenhum resultado encontrado' : 'Nenhum projeto por aqui';
    emptyState.querySelector('p').textContent = projects.length && query
      ? 'Tente buscar usando outro nome de projeto.'
      : 'Comece criando seu primeiro projeto e organize as próximas entregas.';
    document.querySelector('#empty-create').hidden = projects.length > 0 && Boolean(query);
    return;
  }
  filtered.forEach(project => {
    const status = statusFor(project);
    const people = (project.funcionarios || []).slice(0, 3);
    const tr = document.createElement('tr');
    tr.innerHTML = `<td><div class="project-name"><span class="project-icon">▦</span><span><span class="project-title"></span><span class="project-id">PRJ-${String(project.id).padStart(3, '0')}</span></span></div></td><td>${toDate(project.dataInicio)}</td><td>${toDate(project.dataFim)}</td><td><div class="team-avatars">${people.length ? people.map(person => `<span class="team-avatar" title=""></span>`).join('') : '<span class="team-more">Sem equipe</span>'}${(project.funcionarios || []).length > 3 ? `<span class="team-more">+${project.funcionarios.length - 3}</span>` : ''}</div></td><td><span class="status ${status.className}">${status.label}</span></td><td><button class="row-action" title="Ver projeto" aria-label="Ver projeto">···</button></td>`;
    tr.querySelector('.project-title').textContent = project.descricao;
    tr.querySelectorAll('.team-avatar').forEach((avatar, index) => {
      const person = people[index];
      avatar.textContent = initials(person.nome);
      avatar.title = person.nome;
    });
    tr.querySelector('.row-action').addEventListener('click', () => showToast(`${project.descricao} · ${people.length} ${people.length === 1 ? 'pessoa vinculada' : 'pessoas vinculadas'}`));
    list.append(tr);
  });
}

async function loadProjects() {
  const refresh = document.querySelector('#refresh-button');
  refresh.disabled = true;
  try {
    const response = await fetch(API);
    if (!response.ok) throw new Error('Não foi possível carregar os projetos.');
    projects = await response.json();
    render();
  } catch (error) {
    document.querySelector('#result-count').textContent = 'Falha ao carregar os projetos';
    showToast('Não foi possível conectar à API. Tente atualizar a página.');
  } finally {
    refresh.disabled = false;
  }
}

function openDialog() {
  form.reset();
  formError.textContent = '';
  const today = new Date();
  const localDate = new Date(today.getTime() - today.getTimezoneOffset() * 60000).toISOString().slice(0, 10);
  document.querySelector('#dataInicio').value = localDate;
  document.querySelector('#dataFim').value = localDate;
  dialog.showModal();
  document.querySelector('#descricao').focus();
}

document.querySelector('#open-form').addEventListener('click', openDialog);
document.querySelector('#empty-create').addEventListener('click', openDialog);
document.querySelector('#close-form').addEventListener('click', () => dialog.close());
document.querySelector('#cancel-form').addEventListener('click', () => dialog.close());
document.querySelector('#refresh-button').addEventListener('click', loadProjects);
document.querySelector('#search-input').addEventListener('input', render);
document.querySelector('#dataInicio').addEventListener('change', event => {
  document.querySelector('#dataFim').min = event.target.value;
  if (document.querySelector('#dataFim').value < event.target.value) {
    document.querySelector('#dataFim').value = event.target.value;
  }
});

dialog.addEventListener('click', event => {
  if (event.target === dialog) dialog.close();
});

form.addEventListener('submit', async event => {
  event.preventDefault();
  formError.textContent = '';
  const submit = document.querySelector('#submit-button');
  const projeto = {
    descricao: form.elements.descricao.value.trim(),
    dataInicio: form.elements.dataInicio.value,
    dataFim: form.elements.dataFim.value
  };
  if (projeto.dataFim < projeto.dataInicio) {
    formError.textContent = 'A data de entrega não pode ser anterior à data de início.';
    return;
  }
  submit.disabled = true;
  submit.textContent = 'Salvando...';
  try {
    const response = await fetch(API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(projeto)
    });
    const result = await response.json().catch(() => ({}));
    if (!response.ok) {
      const details = Object.values(result.campos || {}).join(' ');
      throw new Error(details || result.mensagem || 'Não foi possível criar o projeto.');
    }
    dialog.close();
    showToast('Projeto criado com sucesso.');
    await loadProjects();
  } catch (error) {
    formError.textContent = error.message;
  } finally {
    submit.disabled = false;
    submit.innerHTML = 'Criar projeto <span>→</span>';
  }
});

loadProjects();
