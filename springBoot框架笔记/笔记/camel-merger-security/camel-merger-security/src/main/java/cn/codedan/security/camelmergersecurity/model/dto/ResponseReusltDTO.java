package cn.codedan.security.camelmergersecurity.model.dto;

import lombok.Data;

@Data
public class ResponseReusltDTO<T> {

    private Integer code;

    private String message;

    private T Data;

    public ResponseReusltDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseReusltDTO(Integer code, T data) {
        this.code = code;
        Data = data;
    }

    public ResponseReusltDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        Data = data;
    }
}
