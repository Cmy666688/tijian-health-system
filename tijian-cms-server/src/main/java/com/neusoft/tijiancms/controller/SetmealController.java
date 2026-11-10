package com.neusoft.tijiancms.controller;
import java.util.List;
import com.neusoft.tijiancms.po.Setmeal;
import com.neusoft.tijiancms.service.SetmealService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    private final SetmealService service;
    public SetmealController(SetmealService service) { this.service=service; }
    @PostMapping("/listSetmeal") public List<Setmeal> list() { return service.list(); }
}
