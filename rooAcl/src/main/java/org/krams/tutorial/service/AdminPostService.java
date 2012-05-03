package org.krams.tutorial.service;

import java.util.List;

import org.krams.tutorial.domain.AdminPost;
import org.springframework.roo.addon.layers.service.RooService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

@RooService(domainTypes = { org.krams.tutorial.domain.AdminPost.class })
public interface AdminPostService {

	//@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract long countAllAdminPosts();

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract void deleteAdminPost(AdminPost adminPost);

	@PostAuthorize("hasPermission(returnObject, 'WRITE')")
	public abstract AdminPost findAdminPost(Long id);

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<AdminPost> findAllAdminPosts();

	@PostFilter("hasPermission(filterObject, 'READ')")
	public abstract List<AdminPost> findAdminPostEntries(int firstResult, int maxResults);


	public abstract void saveAdminPost(AdminPost adminPost);

	@PreAuthorize("hasPermission(#post, 'WRITE')")
	public abstract AdminPost updateAdminPost(AdminPost adminPost);

}
