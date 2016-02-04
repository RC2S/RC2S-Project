package com.rc2s.application.services.cube;

import org.springframework.stereotype.Service;

@Service
public class CubeService implements CubeServiceI {

    @Override
    public String getCube() {
       return "coucou";
    }
}
