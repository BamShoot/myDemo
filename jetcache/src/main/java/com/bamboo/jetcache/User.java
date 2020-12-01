package com.bamboo.jetcache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户实体")
public class User implements Serializable {

    Long id;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty("密码")
    String password;


}
