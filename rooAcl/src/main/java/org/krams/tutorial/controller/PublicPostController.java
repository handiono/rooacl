package org.krams.tutorial.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.krams.tutorial.domain.PublicPost;
import org.krams.tutorial.service.PublicPostService;
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

@RequestMapping("/publicposts")
@Controller
@RooWebScaffold(path = "publicposts", formBackingObject = PublicPost.class)
@RooWebJson(jsonObject = PublicPost.class)
public class PublicPostController {

	@Autowired
    PublicPostService publicPostService;
	
	protected static Logger logger = Logger.getLogger("controller");

	/**
     * Saves a new post from the Add page and returns a result page.
     */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PublicPost publicPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	logger.debug("Received request to view add page");
            // Add date today
        	publicPost.setDate(new Date());
        	// Add source to model to help us determine the source of the JSP page
            uiModel.addAttribute("source", "Public");
            // Add our current role and username
            uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
            populateEditForm(uiModel, publicPost);
            return "publicposts/create";
        }
        uiModel.asMap().clear();
        publicPostService.savePublicPost(publicPost);
        return "redirect:/publicposts/" + encodeUrlPathSegment(publicPost.getId().toString(), httpServletRequest);
    }

	/**
     * Retrieves the Add page
     * <p>
     * Access-control is placed here (instead in the service) because we don't want 
     * to show this page if the client is unauthorized and because the new 
     * object doesn't have an id. The hasPermission requires an existing id!
     */
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
		logger.debug("Received request to show add page");
		// Add source to model to help us determine the source of the JSP page
	    uiModel.addAttribute("source", "Public");
        populateEditForm(uiModel, new PublicPost());
        return "publicposts/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("publicpost", publicPostService.findPublicPost(id));
        uiModel.addAttribute("itemId", id);
        return "publicposts/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("publicposts", publicPostService.findPublicPostEntries(firstResult, sizeNo));
            float nrOfPages = (float) publicPostService.countAllPublicPosts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("publicposts", publicPostService.findAllPublicPosts());
        }
        addDateTimeFormatPatterns(uiModel);
        return "publicposts/list";
    }

	/**
     * Saves the edited post from the Edit page and returns a result page.
     */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PublicPost publicPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

		logger.debug("Received request to view edit page");
	     // Assign new date
	     publicPost.setDate(new Date());
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, publicPost);
            return "publicposts/update";
        }
        uiModel.asMap().clear();
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Public");
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        publicPostService.updatePublicPost(publicPost);
        return "redirect:/publicposts/" + encodeUrlPathSegment(publicPost.getId().toString(), httpServletRequest);
    }

	 /**
     * Retrieves the Edit page
     */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		logger.debug("Received request to show edit page");
        populateEditForm(uiModel, publicPostService.findPublicPost(id));
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Public");
        return "publicposts/update";
    }

	/**
     * Deletes an existing post and returns a result page.
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.debug("Received request to view delete page");
        PublicPost publicPost = publicPostService.findPublicPost(id);
        publicPostService.deletePublicPost(publicPost);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Public");
         
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
         
        return "redirect:/publicposts";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("publicPost_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, PublicPost publicPost) {
        uiModel.addAttribute("publicPost", publicPost);
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
        PublicPost publicPost = publicPostService.findPublicPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (publicPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(publicPost.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<PublicPost> result = publicPostService.findAllPublicPosts();
        return new ResponseEntity<String>(PublicPost.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PublicPost publicPost = PublicPost.fromJsonToPublicPost(json);
        publicPostService.savePublicPost(publicPost);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PublicPost publicPost: PublicPost.fromJsonArrayToPublicPosts(json)) {
            publicPostService.savePublicPost(publicPost);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        PublicPost publicPost = PublicPost.fromJsonToPublicPost(json);
        if (publicPostService.updatePublicPost(publicPost) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (PublicPost publicPost: PublicPost.fromJsonArrayToPublicPosts(json)) {
            if (publicPostService.updatePublicPost(publicPost) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        PublicPost publicPost = publicPostService.findPublicPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (publicPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        publicPostService.deletePublicPost(publicPost);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

}
