package com.neusoft.tijian.controller;
import com.neusoft.tijian.po.Setmeal;
import com.neusoft.tijian.po.CheckItem;
import com.neusoft.tijian.service.SetmealService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    private final SetmealService service;
    public SetmealController(SetmealService service) { this.service = service; }
    @PostMapping("/listSetmealByType")
    public List<Setmeal> listSetmealByType(@RequestBody Setmeal request) {
        if (request.getType() == null || (request.getType() != 0 && request.getType() != 1))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "套餐类型须为0或1");
        return service.listByType(request.getType());
    }
    @PostMapping("/getSetmealById")
    public Setmeal getSetmealById(@RequestBody Setmeal request) {
        if (request.getSmId() == null || request.getSmId() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "套餐编号须为正整数");
        return service.findById(request.getSmId());
    }
    @PostMapping("/listCheckItems")
    public List<CheckItem> listCheckItems(@RequestBody Setmeal request) {
        if (request.getSmId()==null || request.getSmId()<=0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"套餐编号须为正整数");
        if (service.findById(request.getSmId())==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"套餐不存在");
        return service.listCheckItems(request.getSmId());
    }
}
