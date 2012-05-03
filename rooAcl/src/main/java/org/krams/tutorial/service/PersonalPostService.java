package org.krams.tutorial.service;

import java.util.List;
import org.krams.tutorial.domain.PersonalPost;
import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

@RooService(domainTypes = { org.krams.tutorial.domain.PersonalPost.class })
public interface PersonalPostService {

	//@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract long countAllPersonalPosts();

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract void deletePersonalPost(PersonalPost personalPost);

	@PostAuthorize("hasPermission(returnObject, 'WRITE')")
	public abstract PersonalPost findPersonalPost(Long id);

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<PersonalPost> findAllPersonalPosts();

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<PersonalPost> findPersonalPostEntries(int firstResult, int maxResults);


	public abstract void savePersonalPost(PersonalPost personalPost);

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract PersonalPost updatePersonalPost(PersonalPost personalPost);

}
