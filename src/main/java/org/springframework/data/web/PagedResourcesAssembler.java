/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.web;

import static org.springframework.web.util.UriComponentsBuilder.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link ResourceAssembler} to easily convert {@link Page} instances into {@link PagedResources}.
 * 
 * @since 1.6
 * @author Oliver Gierke
 * @author Nick Williams
 */
public class PagedResourcesAssembler<T> implements ResourceAssembler<Page<T>, PagedResources<Resource<T>>> {

	private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;
	private final UriComponents baseUri;
	private final EmbeddedWrappers wrappers = new EmbeddedWrappers(false);

	private boolean forceFirstAndLastRels = false;

	/**
	 * Creates a new {@link PagedResourcesAssembler} using the given {@link PageableHandlerMethodArgumentResolver} and
	 * base URI. If the former is {@literal null}, a default one will be created. If the latter is {@literal null}, calls
	 * to {@link #toResource(Page)} will use the current request's URI to build the relevant previous and next links.
	 * 
	 * @param resolver
	 * @param baseUri
	 */
	public PagedResourcesAssembler(HateoasPageableHandlerMethodArgumentResolver resolver, UriComponents baseUri) {

		this.pageableResolver = resolver == null ? new HateoasPageableHandlerMethodArgumentResolver() : resolver;
		this.baseUri = baseUri;
	}

	/**
	 * Configures whether to always add {@code first} and {@code last} links to the {@link PagedResources} created.
	 * Defaults to {@literal false} which means that {@code first} and {@code last} links only appear in conjunction with
	 * {@code prev} and {@code next} links.
	 * 
	 * @param forceFirstAndLastRels whether to always add {@code first} and {@code last} links to the
	 *          {@link PagedResources} created.
	 * @since 1.11
	 */
	public void setForceFirstAndLastRels(boolean forceFirstAndLastRels) {
		this.forceFirstAndLastRels = forceFirstAndLastRels;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceAssembler#toResource(java.lang.Object)
	 */
	@Override
	public PagedResources<Resource<T>> toResource(Page<T> entity) {
		return toResource(entity, new SimplePagedResourceAssembler<T>());
	}

	/**
	 * Creates a new {@link PagedResources} by converting the given {@link Page} into a {@link PageMetadata} instance and
	 * wrapping the contained elements into {@link Resource} instances. Will add pagination links based on the given the
	 * self link.
	 * 
	 * @param page must not be {@literal null}.
	 * @param selfLink must not be {@literal null}.
	 * @return
	 */
	public PagedResources<Resource<T>> toResource(Page<T> page, Link selfLink) {
		return toResource(page, new SimplePagedResourceAssembler<T>(), selfLink);
	}

	/**
	 * Creates a new {@link PagedResources} by converting the given {@link Page} into a {@link PageMetadata} instance and
	 * using the given {@link ResourceAssembler} to turn elements of the {@link Page} into resources.
	 * 
	 * @param page must not be {@literal null}.
	 * @param assembler must not be {@literal null}.
	 * @return
	 */
	public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> assembler) {
		return createResource(page, assembler, null);
	}

	/**
	 * Creates a new {@link PagedResources} by converting the given {@link Page} into a {@link PageMetadata} instance and
	 * using the given {@link ResourceAssembler} to turn elements of the {@link Page} into resources. Will add pagination
	 * links based on the given the self link.
	 * 
	 * @param page must not be {@literal null}.
	 * @param assembler must not be {@literal null}.
	 * @param link must not be {@literal null}.
	 * @return
	 */
	public <R extends ResourceSupport> PagedResources<R> toResource(Page<T> page, ResourceAssembler<T, R> assembler,
			Link link) {

		Assert.notNull(link, "Link must not be null!");
		return createResource(page, assembler, link);
	}

	/**
	 * Creates a {@link PagedResources} with an empt collection {@link EmbeddedWrapper} for the given domain type.
	 * 
	 * @param page must not be {@literal null}, content must be empty.
	 * @param type must not be {@literal null}.
	 * @param link can be {@literal null}.
	 * @return
	 * @since 1.11
	 */
	public PagedResources<?> toEmptyResource(Page<?> page, Class<?> type, Link link) {

		Assert.notNull(page, "Page must must not be null!");
		Assert.isTrue(!page.hasContent(), "Page must not have any content!");
		Assert.notNull(type, "Type must not be null!");

		PageMetadata metadata = asPageMetadata(page);

		EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(type);
		List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);

		return addPaginationLinks(new PagedResources<EmbeddedWrapper>(embedded, metadata), page, link);
	}

	/**
	 * Adds the pagination parameters for all parameters not already present in the given {@link Link}.
	 * 
	 * @param link must not be {@literal null}.
	 * @return
	 * @deprecated this method will be removed in 1.11 as no Spring Data module actually calls it. Other clients calling
	 *             it should stop doing so as {@link Link}s used for pagination shouldn't be templated in the first place.
	 */
	@Deprecated
	public Link appendPaginationParameterTemplates(Link link) {

		Assert.notNull(link, "Link must not be null!");
		return createLink(new UriTemplate(link.getHref()), null, link.getRel());
	}

	/**
	 * Creates the {@link PagedResources} to be equipped with pagination links downstream.
	 * 
	 * @param resources the original page's elements mapped into {@link ResourceSupport} instances.
	 * @param metadata the calculated {@link PageMetadata}, must not be {@literal null}.
	 * @param page the original page handed to the assembler, must not be {@literal null}.
	 * @return must not be {@literal null}.
	 */
	protected <R extends ResourceSupport, S> PagedResources<R> createPagedResource(List<R> resources,
			PageMetadata metadata, Page<S> page) {

		Assert.notNull(resources, "Content resources must not be null!");
		Assert.notNull(metadata, "PageMetadata must not be null!");
		Assert.notNull(page, "Page must not be null!");

		return new PagedResources<R>(resources, metadata);
	}

	private <S, R extends ResourceSupport> PagedResources<R> createResource(Page<S> page,
			ResourceAssembler<S, R> assembler, Link link) {

		Assert.notNull(page, "Page must not be null!");
		Assert.notNull(assembler, "ResourceAssembler must not be null!");

		List<R> resources = new ArrayList<R>(page.getNumberOfElements());

		for (S element : page) {
			resources.add(assembler.toResource(element));
		}

		PagedResources<R> resource = createPagedResource(resources, asPageMetadata(page), page);

		return addPaginationLinks(resource, page, link);
	}

	private <R> PagedResources<R> addPaginationLinks(PagedResources<R> resources, Page<?> page, Link link) {

		UriTemplate base = getUriTemplate(link);

		boolean isNavigable = page.hasPrevious() || page.hasNext();

		if (isNavigable || forceFirstAndLastRels) {
			resources.add(createLink(base, new PageRequest(0, page.getSize(), page.getSort()), Link.REL_FIRST));
		}

		if (page.hasPrevious()) {
			resources.add(createLink(base, page.previousPageable(), Link.REL_PREVIOUS));
		}

		Pageable current = new PageRequest(page.getNumber(), page.getSize(), page.getSort());

		resources.add(link == null ? createLink(base, current, Link.REL_SELF) : link.withSelfRel());

		if (page.hasNext()) {
			resources.add(createLink(base, page.nextPageable(), Link.REL_NEXT));
		}

		if (isNavigable || forceFirstAndLastRels) {

			int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;

			resources.add(createLink(base, new PageRequest(lastIndex, page.getSize(), page.getSort()), Link.REL_LAST));
		}

		return resources;
	}

	/**
	 * Returns a default URI string either from the one configured on assembler creatino or by looking it up from the
	 * current request.
	 * 
	 * @return
	 */
	private UriTemplate getUriTemplate(Link baseLink) {

		String href = baseLink != null ? baseLink.getHref()
				: baseUri == null ? ServletUriComponentsBuilder.fromCurrentRequest().build().toString() : baseUri.toString();

		return new UriTemplate(href);
	}

	/**
	 * Creates a {@link Link} with the given rel that will be based on the given {@link UriTemplate} but enriched with the
	 * values of the given {@link Pageable} (if not {@literal null}).
	 * 
	 * @param base must not be {@literal null}.
	 * @param pageable can be {@literal null}
	 * @param rel must not be {@literal null} or empty.
	 * @return
	 */
	private Link createLink(UriTemplate base, Pageable pageable, String rel) {

		UriComponentsBuilder builder = fromUri(base.expand());
		pageableResolver.enhance(builder, getMethodParameter(), pageable);

		return new Link(new UriTemplate(builder.build().toString()), rel);
	}

	/**
	 * Return the {@link MethodParameter} to be used to potentially qualify the paging and sorting request parameters to.
	 * Default implementations returns {@literal null}, which means the parameters will not be qualified.
	 * 
	 * @return
	 * @since 1.7
	 */
	protected MethodParameter getMethodParameter() {
		return null;
	}

	/**
	 * Creates a new {@link PageMetadata} instance from the given {@link Page}.
	 * 
	 * @param page must not be {@literal null}.
	 * @return
	 */
	private static <T> PageMetadata asPageMetadata(Page<T> page) {

		Assert.notNull(page, "Page must not be null!");
		return new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
	}

	private static class SimplePagedResourceAssembler<T> implements ResourceAssembler<T, Resource<T>> {

		@Override
		public Resource<T> toResource(T entity) {
			return new Resource<T>(entity);
		}
	}
}
