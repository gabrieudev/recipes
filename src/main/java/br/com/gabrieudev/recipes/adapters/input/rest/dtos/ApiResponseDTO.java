package br.com.gabrieudev.recipes.adapters.input.rest.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    @Schema(
        description = "Código de status da resposta",
        example = "200"
    )
    private Integer status;

    @Schema(
        description = "Dados da resposta"
    )
    private T data;

    @Schema(
        description = "Data e hora da resposta",
        example = "2023-01-01T00:00:00"
    )
    private LocalDateTime timestamp;

    public static <T> ApiResponseDTO<T> ok(T data) {
        return new ApiResponseDTO<>(200, data, LocalDateTime.now());
    }

    public static <T> ApiResponseDTO<T> created(T data) {
        return new ApiResponseDTO<>(201, data, LocalDateTime.now());
    }

    public static <T> ApiResponseDTO<T> noContent(T data) {
        return new ApiResponseDTO<>(204, data, LocalDateTime.now());
    }

    public static <T> ApiResponseDTO<T> error(T data, Integer status) {
        return new ApiResponseDTO<>(status, data, LocalDateTime.now());
    }
}
