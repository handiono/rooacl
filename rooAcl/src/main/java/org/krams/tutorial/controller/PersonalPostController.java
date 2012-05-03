package org.krams.tutorial.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.krams.tutorial.domain.PersonalPost;
import org.krams.tutorial.service.PersonalPostService;
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

@RequestMapping("/personalposts")
@Controller
@RooWebScaffold(path = "personalposts", formBackingObject = PersonalPost.class)
@RooWebJson(jsonObject = PersonalPost.class)
public class PersonalPostController {

	 protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
    PersonalPostService personalPostService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PersonalPost personalPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalPost);
            return "personalposts/create";
        }
        uiModel.asMap().clear();
        personalPostService.savePersonalPost(personalPost);
        return "redirect:/personalposts/" + encodeUrlPathSegment(personalPost.getId().toString(), httpServletRequest);
    }

    /**
     * Retrieves the Add page
     * <p>
     * Access-control is placed here (instead in the service) because we don't want 
     * to show this page if the client is unauthorized and because the new 
     * object doesn't have an id. The hasPermission requires an existing id!
     */
	@PreAuthorize("hasAuthority('ROLE_USER') and !hasAuthority('ROLE_ADMIN')")	
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
		logger.debug("Received request to show add page");
        populateEditForm(uiModel, new PersonalPost());
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Personal");
        return "personalposts/create";
    }
 
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("personalpost", personalPostService.findPersonalPost(id));
        uiModel.addAttribute("itemId", id);
        return "personalposts/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("personalposts", personalPostService.findPersonalPostEntries(firstResult, sizeNo));
            float nrOfPages = (float) personalPostService.countAllPersonalPosts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("personalposts", personalPostService.findAllPersonalPosts());
        }
        addDateTimeFormatPatterns(uiModel);
        return "personalposts/list";
    }
	
	 /**
     * Saves the edited post from the Edit page and returns a result page.
     */
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PersonalPost personalPost, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		logger.debug("Received request to view edit page");
		personalPost.setDate(new Date());
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, personalPost);
            return "personalposts/update";
        }
        uiModel.asMap().clear();
        personalPostService.updatePersonalPost(personalPost);
        uiModel.addAttribute("source", "Personal");
        
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
         
        return "redirect:/personalposts/" + encodeUrlPathSegment(personalPost.getId().toString(), httpServletRequest);
    }

	/**
     * Retrieves the Edit page
     */
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {

		// Retrieve existing post and add to model
	     // This is the formBackingOBject
		logger.debug("Received request to show edit page");
		
		// Add source to model to help us determine the source of the JSP page
	     uiModel.addAttribute("source", "Personal");
	     
        populateEditForm(uiModel, personalPostService.findPersonalPost(id));
        return "personalposts/update";
    }

	/**
     * Deletes an existing post and returns a result page.
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.debug("Received request to view delete page");
		PersonalPost personalPost = personalPostService.findPersonalPost(id);
        personalPostService.deletePersonalPost(personalPost);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        // Add source to model to help us determine the source of the JSP page
        uiModel.addAttribute("source", "Personal");
         
        // Add our current role and username
        uiModel.addAttribute("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        uiModel.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
         
        return "redirect:/personalposts";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("personalPost_date_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, PersonalPost personalPost) {
        uiModel.addAttribute("personalPost", personalPost);
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
        PersonalPost personalPost = personalPostService.findPersonalPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (personalPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(personalPost.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<PersonalPost> result = personalPostService.findAllPersonalPosts();
        return new ResponseEntity<String>(PersonalPost.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PersonalPost personalPost = PersonalPost.fromJsonToPersonalPost(json);
        personalPostService.savePersonalPost(personalPost);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PersonalPost personalPost: PersonalPost.fromJsonArrayToPersonalPosts(json)) {
            personalPostService.savePersonalPost(personalPost);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        PersonalPost personalPost = PersonalPost.fromJsonToPersonalPost(json);
        if (personalPostService.updatePersonalPost(personalPost) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (PersonalPost personalPost: PersonalPost.fromJsonArrayToPersonalPosts(json)) {
            if (personalPostService.updatePersonalPost(personalPost) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        PersonalPost personalPost = personalPostService.findPersonalPost(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (personalPost == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        personalPostService.deletePersonalPost(personalPost);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
