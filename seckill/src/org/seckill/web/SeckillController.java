package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {

		List<Seckill> list = seckillService.getSeckillList();

		model.addAttribute("list", list);

		return "list";
	}

	@RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String exposer(@PathVariable("seckillId") Long seckillId) {

		SeckillResult<Exposer> result;

		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = new SeckillResult<>(false, e.getMessage());
		}

//		return result;
		return JSONObject.toJSON(result).toString();

	}

	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String execute(@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5, @CookieValue(value = "killPhone", required = false) Long phone) {

		if (phone == null) {
//			return new SeckillResult<SeckillExecution>(false, "δע�ᣡ");
			return JSONObject.toJSON(new SeckillResult<SeckillExecution>(false, "δע�ᣡ")).toString();
		}

		SeckillResult<SeckillExecution> result;

		try {
			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			result = new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			result = new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
			result = new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillException e) {
			logger.error(e.getMessage(), e);
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
			result = new SeckillResult<SeckillExecution>(true, execution);
		}

//		return result;
		return JSONObject.toJSON(result).toString();
		
	}

	@RequestMapping(value = "/time/now", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String time() {

		Date now = new Date();
//		return new SeckillResult<Long>(true, now.getTime());
		return JSONObject.toJSON(new SeckillResult<Long>(true, now.getTime())).toString();

	}
	
	

}
