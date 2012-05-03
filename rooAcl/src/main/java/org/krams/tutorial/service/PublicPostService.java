package org.krams.tutorial.service;

import java.util.List;
import org.krams.tutorial.domain.PublicPost;
import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

@RooService(domainTypes = { org.krams.tutorial.domain.PublicPost.class })
public interface PublicPostService {

	//@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract long countAllPublicPosts();

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract void deletePublicPost(PublicPost publicPost);

	@PostAuthorize("hasPermission(returnObject, 'WRITE')")
	public abstract PublicPost findPublicPost(Long id);

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<PublicPost> findAllPublicPosts();

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<PublicPost> findPublicPostEntries(int firstResult, int maxResults);


	public abstract void savePublicPost(PublicPost publicPost);

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract PublicPost updatePublicPost(PublicPost publicPost);

}
