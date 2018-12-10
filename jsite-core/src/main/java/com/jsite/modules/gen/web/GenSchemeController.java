package com.jsite.modules.gen.web;

import com.jsite.common.config.Global;
import com.jsite.common.lang.StringUtils;
import com.jsite.common.persistence.Page;
import com.jsite.common.web.BaseController;
import com.jsite.modules.gen.entity.GenScheme;
import com.jsite.modules.gen.service.GenSchemeService;
import com.jsite.modules.gen.service.GenTableService;
import com.jsite.modules.gen.util.GenUtils;
import com.jsite.modules.sys.entity.User;
import com.jsite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成方案Controller
 * @author liuruijun
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genScheme")
public class GenSchemeController extends BaseController {

	@Autowired
	private GenSchemeService genSchemeService;
	
	@Autowired
	private GenTableService genTableService;
	
	@ModelAttribute
	public GenScheme get(@RequestParam(required=false) String id) {
//		if (StringUtils.isNotBlank(id)){
//			return genSchemeService.get(id);
//		}else{
//			return new GenScheme();
//		}
		
		GenScheme entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = genSchemeService.get(id);
		}
		if (entity == null){
			entity = new GenScheme();
			entity.setIsNewRecord(true);
		}
		return entity;
	}
	
	@RequiresPermissions("gen:genScheme:view")
	@RequestMapping(value = {"list", ""})
	public String list(GenScheme genScheme, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/gen/genSchemeList";
	}
	
	@RequiresPermissions("gen:genScheme:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<GenScheme> listData(GenScheme genScheme, HttpServletRequest request, HttpServletResponse response) {
		
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			genScheme.setCreateBy(user);
		}
        Page<GenScheme> page = genSchemeService.find(new Page<GenScheme>(request, response), genScheme); 
		return page;
	}

	@RequiresPermissions("gen:genScheme:view")
	@RequestMapping(value = "form")
	public String form(GenScheme genScheme, Model model) {
		if (StringUtils.isBlank(genScheme.getPackageName())){
			genScheme.setPackageName("com.jsite.modules");
		}
//		if (StringUtils.isBlank(genScheme.getFunctionAuthor())){
//			genScheme.setFunctionAuthor(UserUtils.getUser().getName());
//		}
		model.addAttribute("genScheme", genScheme);
		model.addAttribute("config", GenUtils.getConfig());
		model.addAttribute("tableList", genTableService.findAll());
		return "modules/gen/genSchemeForm";
	}

	@RequiresPermissions("gen:genScheme:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(GenScheme genScheme, Model model, RedirectAttributes redirectAttributes) {
//		if (!beanValidator(model, genScheme)){
//			return form(genScheme, model);
//		}
		
		String result = genSchemeService.save(genScheme);
//		addMessage(redirectAttributes, "操作生成方案'" + genScheme.getName() + "'成功<br/>"+result);
//		return "redirect:" + adminPath + "/gen/genScheme/?repage";
		return renderResult(Global.TRUE, "操作生成方案'" + genScheme.getName() + "'成功<br/>" + result);
	}
	
	@RequiresPermissions("gen:genScheme:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(GenScheme genScheme, RedirectAttributes redirectAttributes) {
		genSchemeService.delete(genScheme);
//		addMessage(redirectAttributes, "删除生成方案成功");
//		return "redirect:" + adminPath + "/gen/genScheme/?repage";
		return renderResult(Global.TRUE, "删除生成方案成功");
	}

}
