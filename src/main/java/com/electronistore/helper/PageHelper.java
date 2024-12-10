package com.electronistore.helper;

import com.electronistore.dto.PageableResponse;
import com.electronistore.dto.UserDto;
import com.electronistore.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PageHelper {

    // TODO: 05-10-2024  Very important concept. 
    //-> U is entity class and V is dto class
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){

        List<U> entity= page.getContent();

        List<V> dtoList = new ArrayList<>();
        for (U object: entity) {
            V dto = new ModelMapper().map(object,type);
            dtoList.add(dto);
        }

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());
        
        return response;

    }
}
