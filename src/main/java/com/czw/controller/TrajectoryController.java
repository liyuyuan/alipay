package com.czw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/trajectory")
public class TrajectoryController {
	/*
	 * 所有VIP的top1轨迹接口
	 */
	@PostMapping("/topOne")
	public ResponseEntity<?> topOne(HttpServletRequest request){
		List<String> cameraIdList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			cameraIdList.add(i+"");
		}
		
		return new ResponseEntity<>(cameraIdList,HttpStatus.OK);
	}

}
