package com.appNgeek.spring_entity_audit_demo.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appNgeek.spring_entity_audit_demo.domain.Article;
import com.appNgeek.spring_entity_audit_demo.exception.BlogAppException;
import com.appNgeek.spring_entity_audit_demo.repo.ArticleRepository;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;

	@GetMapping
	@Cacheable("Article_Response_Page")
	public Page<Article> findAll(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@GetMapping("/list")
	@Cacheable("Article_Response_List")
	public List<Article> findAllAsList(Pageable pageable) {
		return articleRepository.findAll(pageable).getContent();
	}

	@GetMapping("/find")
	@Cacheable("Article_Response")
	public Article findByTitle(@RequestParam String title) {
		return articleRepository.findByTitle(title);
	}

	@GetMapping("/{id}")
	@Cacheable("Article_Response")
	public Article findOne(@PathVariable Long id) throws BlogAppException {
		Optional<Article> result = articleRepository.findById(id);
		if (result.isPresent())
			return result.get();
		else
			throw new BlogAppException("Article with given id not found");
	}

	@PostMapping
	@Caching(put = {@CachePut(value = "Article_Response")}, evict = {@CacheEvict(value = "Article_Response_List", allEntries = true)})
	public Article create(@RequestBody Article article) {
		return articleRepository.save(article);
	}
	
	@PutMapping
	@Caching(put = {@CachePut(value = "Article_Response")}, evict = {@CacheEvict(value = "Article_Response_List", allEntries = true)})
	public Article update(Article article) {
		return articleRepository.save(article);
	}

	@DeleteMapping("/{id}")
	@Caching(evict = {@CacheEvict(value = "Article_Response"),@CacheEvict(value = "Article_Response_List", allEntries = true)})
	public void delete(@PathVariable Long id) {
		articleRepository.deleteById(id);
	}

}
