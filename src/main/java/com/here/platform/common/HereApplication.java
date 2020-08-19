package com.here.platform.common;

import lombok.Data;


@Data
public class HereApplication {
    String appName;
    String appId;
    String appClientId;
    String appKeyId;
    String appKeySecret;
}