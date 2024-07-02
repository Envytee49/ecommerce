package org.example.ecommerce.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    protected int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String message;

}
