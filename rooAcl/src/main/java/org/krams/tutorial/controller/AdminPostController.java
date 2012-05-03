package org.krams.tutorial.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.krams.tutorial.domain.AdminPost;
import org.krams.tutorial.service.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/adminposts")
@Controller
@RooWebScaffold(path = "adminposts", formBackingObject = AdminPost.class)
@RooWebJson(jsonObject = AdminPost.class)
public class AdminPostController {

	@Autowired
    AdminPostService adminPostService;
	
	protected static Logger logger = Logger.getLogger("controller");

	 /**
     * Saves a new post from the Add page and returns a result page.
     */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AdminPost adminPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		logger.debug("Received request to view add page");
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, adminPost);
            return "adminposts/create";
        }
        uiModel.asMap().clear();
        adminPost.setDate(new Date());
        adminPostService.saveAdminPost(adminPost);
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Admin");
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        // This will resolve to /WEB-INF/jsp/crud-admin/resultpage.jsp
        //return "crud-admin/resultpage";
        return "redirect:/adminposts/" + encodeUrlPathSegment(adminPost.getId().toString(), httpServletRequest);
    }
	 
	 /**
     * Retrieves the Add page
     * <p>
     * Access-control is placed here (instead in the service) because we don't want 
     * to show this page if the client is unauthorized and because the new 
     * object doesn't have an id. The hasPermission requires an existing id!
     */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
		logger.debug("Received request to show add page");
        populateEditForm(uiModel, new AdminPost());
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Admin");
         
        // This will resolve to /WEB-INF/jsp/crud-admin/addpage.jsp
//        return "crud-admin/addpage";
        return "adminposts/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("adminpost", adminPostService.findAdminPost(id));
        uiModel.addAttribute("itemId", id);
        return "adminposts/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("adminposts", adminPostService.findAdminPostEntries(firstResult, sizeNo));
            float nrOfPages = (float) adminPostService.countAllAdminPosts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("adminposts", adminPostService.findAllAdminPosts());
        }
        addDateTimeFormatPatterns(uiModel);
        return "adminposts/list";
    }

	 /**
     * Saves the edited post from the Edit page and returns a result page.
     */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AdminPost adminPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, adminPost);
            return "adminposts/update";
        }
        uiModel.asMap().clear();
        adminPost.setDate(new Date());
        uiModel.addAttribute("source", "Admin");
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        adminPostService.updateAdminPost(adminPost);
        // This will resolve to /WEB-INF/jsp/crud-admin/resultpage.jsp
//        return "crud-admin/resultpage";
        return "redirect:/adminposts/" + encodeUrlPathSegment(adminPost.getId().toString(), httpServletRequest);
    }

	/**
     * Retrieves the Edit page
     */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		
		logger.debug("Received request to show edit page");
	     
	     // Retrieve existing post and add to model
	     // This is the formBackingOBject
        populateEditForm(uiModel, adminPostService.findAdminPost(id));
        // This will resolve to /WEB-INF/jsp/crud-admin/editpage.jsp
//        return "crud-admin/editpage";
        return "adminposts/update";
    }
	
	/**
     * Deletes an existing post and returns a result page.
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.debug("Received request to view delete page");
		AdminPost adminPost = adminPostService.findAdminPost(id);
        adminPostService.deleteAdminPost(adminPost);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Admin");
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        // This will resolve to /WEB-INF/jsp/crud-admin/resultpage.jsp
//        return "crud-admin/resultpage";
        return "redirect:/adminposts";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("adminPost_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, AdminPost adminPost) {
        uiModel.addAttribute("adminPost", adminPost);
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Admin");
        addDateTimeFormatPatterns(uiModel);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        AdminPost adminPost = adminPostService.findAdminPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (adminPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(adminPost.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AdminPost> result = adminPostService.findAllAdminPosts();
        return new ResponseEntity<String>(AdminPost.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        AdminPost adminPost = AdminPost.fromJsonToAdminPost(json);
        adminPostService.saveAdminPost(adminPost);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (AdminPost adminPost: AdminPost.fromJsonArrayToAdminPosts(json)) {
            adminPostService.saveAdminPost(adminPost);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AdminPost adminPost = AdminPost.fromJsonToAdminPost(json);
        if (adminPostService.updateAdminPost(adminPost) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AdminPost adminPost: AdminPost.fromJsonArrayToAdminPosts(json)) {
            if (adminPostService.updateAdminPost(adminPost) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        AdminPost adminPost = adminPostService.findAdminPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (adminPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        adminPostService.deleteAdminPost(adminPost);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
