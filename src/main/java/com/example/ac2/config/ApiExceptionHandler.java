package com.example.ac2.config;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.ac2.config.exceptions.RegraNegocioException;
import com.example.ac2.dto.ErroDTO;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroDTO> tratarRegraNegocio(RegraNegocioException exception, WebRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> tratarValidacao(MethodArgumentNotValidException exception, WebRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> campos.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Revise os campos informados.", request, campos);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroDTO> tratarRequisicaoInvalida(Exception exception, WebRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "Requisição inválida. Confira os dados e o formato das datas.", request,
                Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> tratarIntegridade(DataIntegrityViolationException exception, WebRequest request) {
        return resposta(HttpStatus.CONFLICT, "Não foi possível concluir a operação por conflito de dados.", request,
                Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> tratarErroInterno(Exception exception, WebRequest request) {
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.", request, Map.of());
    }

    private ResponseEntity<ErroDTO> resposta(HttpStatus status, String mensagem, WebRequest request,
            Map<String, String> campos) {
        String caminho = request instanceof ServletWebRequest servletRequest
                ? servletRequest.getRequest().getRequestURI()
                : "";
        ErroDTO erro = new ErroDTO(Instant.now(), status.value(), status.getReasonPhrase(), mensagem, caminho, campos);
        return ResponseEntity.status(status).body(erro);
    }
}
