package com.yosypchuk.tracker.api;

import com.yosypchuk.tracker.model.DTO.CategoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Activity category management API")
@RequestMapping("/api/v1/activity/category")
public interface CategoryApi {

    @ApiOperation("Get all activity categories")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @ApiResponse(code = 200, message = "OK")
    List<CategoryDTO> getAllActivityCategories();

    @ApiOperation("Get activity category by name")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{name}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    CategoryDTO getActivityCategory(@PathVariable String name);

    @ApiOperation("Create activity category")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiResponses({
            @ApiResponse(code = 201, message = "OK"),
            @ApiResponse(code = 409, message = "Conflict")
    })
    CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDTO);

    @ApiOperation("Delete activity category by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<Void> removeCategory(@PathVariable Long id);
}
