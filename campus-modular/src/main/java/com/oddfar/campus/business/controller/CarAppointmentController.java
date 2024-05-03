package com.oddfar.campus.business.controller;

import com.oddfar.campus.business.dto.CarAppointmentCreateDTO;
import com.oddfar.campus.business.dto.CarAppointmentModifyDTO;
import com.oddfar.campus.business.dto.CarAppointmentSubmitDTO;
import com.oddfar.campus.business.service.ICarAppointmentService;
import com.oddfar.campus.common.annotation.ApiResource;
import com.oddfar.campus.common.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/car/submit")
@ApiResource(name = "车辆预约Controller")
@RequiredArgsConstructor
public class CarAppointmentController {

    private final ICarAppointmentService carAppointmentService;

    @PostMapping(value = "/create", name = "创建")
    @PreAuthorize("@ss.resourceAuth()")
    public R create(@RequestBody @Valid CarAppointmentCreateDTO createDTO) {
        return R.ok(carAppointmentService.create(createDTO));
    }

    @PostMapping(value = "/modify", name = "修改")
    @PreAuthorize("@ss.resourceAuth()")
    public R modify(@RequestBody @Valid CarAppointmentModifyDTO modifyDTO) {
        return R.ok(carAppointmentService.modify(modifyDTO));
    }

    @GetMapping(value = "/sendCode", name = "发送验证码")
    @PreAuthorize("@ss.resourceAuth()")
    public R sendCode(@RequestParam Long id) {
        carAppointmentService.sendCode(id);
        return R.ok();
    }

    @PostMapping(value = "/submit", name = "提交")
    @PreAuthorize("@ss.resourceAuth()")
    public R submit(@RequestBody @Valid CarAppointmentSubmitDTO dto) {
        carAppointmentService.submit(dto);
        return R.ok();
    }

}
