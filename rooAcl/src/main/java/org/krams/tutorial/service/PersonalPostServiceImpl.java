package org.krams.tutorial.service;

import java.util.List;
import org.krams.tutorial.domain.PersonalPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonalPostServiceImpl implements PersonalPostService {

	public long countAllPersonalPosts() {
        return PersonalPost.countPersonalPosts();
    }

	public void deletePersonalPost(PersonalPost personalPost) {
        personalPost.remove();
    }

	public PersonalPost findPersonalPost(Long id) {
        return PersonalPost.findPersonalPost(id);
    }

	public List<PersonalPost> findAllPersonalPosts() {
        return PersonalPost.findAllPersonalPosts();
    }

	public List<PersonalPost> findPersonalPostEntries(int firstResult, int maxResults) {
        return PersonalPost.findPersonalPostEntries(firstResult, maxResults);
    }

	public void savePersonalPost(PersonalPost personalPost) {
        personalPost.persist();
    }

	public PersonalPost updatePersonalPost(PersonalPost personalPost) {
        return personalPost.merge();
    }
}
