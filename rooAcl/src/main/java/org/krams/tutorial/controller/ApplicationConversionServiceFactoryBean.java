package org.krams.tutorial.controller;

import org.krams.tutorial.acl.AclClass;
import org.krams.tutorial.acl.AclEntry;
import org.krams.tutorial.acl.AclObjectIdentity;
import org.krams.tutorial.acl.AclSid;
import org.krams.tutorial.domain.AdminPost;
import org.krams.tutorial.domain.PersonalPost;
import org.krams.tutorial.domain.PublicPost;
import org.krams.tutorial.service.AdminPostService;
import org.krams.tutorial.service.PersonalPostService;
import org.krams.tutorial.service.PublicPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    AdminPostService adminPostService;

	@Autowired
    PersonalPostService personalPostService;

	@Autowired
    PublicPostService publicPostService;

	public Converter<AclClass, String> getAclClassToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.acl.AclClass, java.lang.String>() {
            public String convert(AclClass aclClass) {
                return new StringBuilder().append(aclClass.getClazz()).toString();
            }
        };
    }

	public Converter<Long, AclClass> getIdToAclClassConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.acl.AclClass>() {
            public org.krams.tutorial.acl.AclClass convert(java.lang.Long id) {
                return AclClass.findAclClass(id);
            }
        };
    }

	public Converter<String, AclClass> getStringToAclClassConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.acl.AclClass>() {
            public org.krams.tutorial.acl.AclClass convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AclClass.class);
            }
        };
    }

	public Converter<AclEntry, String> getAclEntryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.acl.AclEntry, java.lang.String>() {
            public String convert(AclEntry aclEntry) {
                return new StringBuilder().append(aclEntry.getAceOrder()).append(" ").append(aclEntry.getMask()).toString();
            }
        };
    }

	public Converter<Long, AclEntry> getIdToAclEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.acl.AclEntry>() {
            public org.krams.tutorial.acl.AclEntry convert(java.lang.Long id) {
                return AclEntry.findAclEntry(id);
            }
        };
    }

	public Converter<String, AclEntry> getStringToAclEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.acl.AclEntry>() {
            public org.krams.tutorial.acl.AclEntry convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AclEntry.class);
            }
        };
    }

	public Converter<AclObjectIdentity, String> getAclObjectIdentityToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.acl.AclObjectIdentity, java.lang.String>() {
            public String convert(AclObjectIdentity aclObjectIdentity) {
                return new StringBuilder().append(aclObjectIdentity.getObjectIdIdentity()).toString();
            }
        };
    }

	public Converter<Long, AclObjectIdentity> getIdToAclObjectIdentityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.acl.AclObjectIdentity>() {
            public org.krams.tutorial.acl.AclObjectIdentity convert(java.lang.Long id) {
                return AclObjectIdentity.findAclObjectIdentity(id);
            }
        };
    }

	public Converter<String, AclObjectIdentity> getStringToAclObjectIdentityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.acl.AclObjectIdentity>() {
            public org.krams.tutorial.acl.AclObjectIdentity convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AclObjectIdentity.class);
            }
        };
    }

	public Converter<AclSid, String> getAclSidToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.acl.AclSid, java.lang.String>() {
            public String convert(AclSid aclSid) {
                return new StringBuilder().append(aclSid.getSid()).toString();
            }
        };
    }

	public Converter<Long, AclSid> getIdToAclSidConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.acl.AclSid>() {
            public org.krams.tutorial.acl.AclSid convert(java.lang.Long id) {
                return AclSid.findAclSid(id);
            }
        };
    }

	public Converter<String, AclSid> getStringToAclSidConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.acl.AclSid>() {
            public org.krams.tutorial.acl.AclSid convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AclSid.class);
            }
        };
    }

	public Converter<AdminPost, String> getAdminPostToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.domain.AdminPost, java.lang.String>() {
            public String convert(AdminPost adminPost) {
                return new StringBuilder().append(adminPost.getDate()).append(" ").append(adminPost.getMessage()).toString();
            }
        };
    }

	public Converter<Long, AdminPost> getIdToAdminPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.domain.AdminPost>() {
            public org.krams.tutorial.domain.AdminPost convert(java.lang.Long id) {
                return adminPostService.findAdminPost(id);
            }
        };
    }

	public Converter<String, AdminPost> getStringToAdminPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.domain.AdminPost>() {
            public org.krams.tutorial.domain.AdminPost convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), AdminPost.class);
            }
        };
    }

	public Converter<PersonalPost, String> getPersonalPostToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.domain.PersonalPost, java.lang.String>() {
            public String convert(PersonalPost personalPost) {
                return new StringBuilder().append(personalPost.getDate()).append(" ").append(personalPost.getMessage()).toString();
            }
        };
    }

	public Converter<Long, PersonalPost> getIdToPersonalPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.domain.PersonalPost>() {
            public org.krams.tutorial.domain.PersonalPost convert(java.lang.Long id) {
                return personalPostService.findPersonalPost(id);
            }
        };
    }

	public Converter<String, PersonalPost> getStringToPersonalPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.domain.PersonalPost>() {
            public org.krams.tutorial.domain.PersonalPost convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PersonalPost.class);
            }
        };
    }

	public Converter<PublicPost, String> getPublicPostToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.krams.tutorial.domain.PublicPost, java.lang.String>() {
            public String convert(PublicPost publicPost) {
                return new StringBuilder().append(publicPost.getDate()).append(" ").append(publicPost.getMessage()).toString();
            }
        };
    }

	public Converter<Long, PublicPost> getIdToPublicPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, org.krams.tutorial.domain.PublicPost>() {
            public org.krams.tutorial.domain.PublicPost convert(java.lang.Long id) {
                return publicPostService.findPublicPost(id);
            }
        };
    }

	public Converter<String, PublicPost> getStringToPublicPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.krams.tutorial.domain.PublicPost>() {
            public org.krams.tutorial.domain.PublicPost convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PublicPost.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getAclClassToStringConverter());
        registry.addConverter(getIdToAclClassConverter());
        registry.addConverter(getStringToAclClassConverter());
        registry.addConverter(getAclEntryToStringConverter());
        registry.addConverter(getIdToAclEntryConverter());
        registry.addConverter(getStringToAclEntryConverter());
        registry.addConverter(getAclObjectIdentityToStringConverter());
        registry.addConverter(getIdToAclObjectIdentityConverter());
        registry.addConverter(getStringToAclObjectIdentityConverter());
        registry.addConverter(getAclSidToStringConverter());
        registry.addConverter(getIdToAclSidConverter());
        registry.addConverter(getStringToAclSidConverter());
        registry.addConverter(getAdminPostToStringConverter());
        registry.addConverter(getIdToAdminPostConverter());
        registry.addConverter(getStringToAdminPostConverter());
        registry.addConverter(getPersonalPostToStringConverter());
        registry.addConverter(getIdToPersonalPostConverter());
        registry.addConverter(getStringToPersonalPostConverter());
        registry.addConverter(getPublicPostToStringConverter());
        registry.addConverter(getIdToPublicPostConverter());
        registry.addConverter(getStringToPublicPostConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
