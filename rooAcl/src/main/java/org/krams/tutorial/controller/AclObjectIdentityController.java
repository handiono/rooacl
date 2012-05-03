package org.krams.tutorial.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.krams.tutorial.acl.AclClass;
import org.krams.tutorial.acl.AclObjectIdentity;
import org.krams.tutorial.acl.AclSid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
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

@RequestMapping("/aclobjectidentitys")
@Controller
@RooWebScaffold(path = "aclobjectidentitys", formBackingObject = AclObjectIdentity.class)
@RooWebJson(jsonObject = AclObjectIdentity.class)
public class AclObjectIdentityController {

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        AclObjectIdentity aclObjectIdentity = AclObjectIdentity.findAclObjectIdentity(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (aclObjectIdentity == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(aclObjectIdentity.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AclObjectIdentity> result = AclObjectIdentity.findAllAclObjectIdentitys();
        return new ResponseEntity<String>(AclObjectIdentity.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        AclObjectIdentity aclObjectIdentity = AclObjectIdentity.fromJsonToAclObjectIdentity(json);
        aclObjectIdentity.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (AclObjectIdentity aclObjectIdentity: AclObjectIdentity.fromJsonArrayToAclObjectIdentitys(json)) {
            aclObjectIdentity.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AclObjectIdentity aclObjectIdentity = AclObjectIdentity.fromJsonToAclObjectIdentity(json);
        if (aclObjectIdentity.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AclObjectIdentity aclObjectIdentity: AclObjectIdentity.fromJsonArrayToAclObjectIdentitys(json)) {
            if (aclObjectIdentity.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        AclObjectIdentity aclObjectIdentity = AclObjectIdentity.findAclObjectIdentity(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (aclObjectIdentity == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        aclObjectIdentity.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AclObjectIdentity aclObjectIdentity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, aclObjectIdentity);
            return "aclobjectidentitys/create";
        }
        uiModel.asMap().clear();
        aclObjectIdentity.persist();
        return "redirect:/aclobjectidentitys/" + encodeUrlPathSegment(aclObjectIdentity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AclObjectIdentity());
        return "aclobjectidentitys/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("aclobjectidentity", AclObjectIdentity.findAclObjectIdentity(id));
        uiModel.addAttribute("itemId", id);
        return "aclobjectidentitys/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("aclobjectidentitys", AclObjectIdentity.findAclObjectIdentityEntries(firstResult, sizeNo));
            float nrOfPages = (float) AclObjectIdentity.countAclObjectIdentitys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("aclobjectidentitys", AclObjectIdentity.findAllAclObjectIdentitys());
        }
        return "aclobjectidentitys/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AclObjectIdentity aclObjectIdentity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, aclObjectIdentity);
            return "aclobjectidentitys/update";
        }
        uiModel.asMap().clear();
        aclObjectIdentity.merge();
        return "redirect:/aclobjectidentitys/" + encodeUrlPathSegment(aclObjectIdentity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AclObjectIdentity.findAclObjectIdentity(id));
        return "aclobjectidentitys/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AclObjectIdentity aclObjectIdentity = AclObjectIdentity.findAclObjectIdentity(id);
        aclObjectIdentity.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/aclobjectidentitys";
    }

	void populateEditForm(Model uiModel, AclObjectIdentity aclObjectIdentity) {
        uiModel.addAttribute("aclObjectIdentity", aclObjectIdentity);
        uiModel.addAttribute("aclclasses", AclClass.findAllAclClasses());
        uiModel.addAttribute("aclobjectidentitys", AclObjectIdentity.findAllAclObjectIdentitys());
        uiModel.addAttribute("aclsids", AclSid.findAllAclSids());
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
}
