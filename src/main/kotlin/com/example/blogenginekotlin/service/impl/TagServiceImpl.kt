package com.example.blogenginekotlin.service.impl

import com.example.blogenginekotlin.dto.TagDto
import com.example.blogenginekotlin.dto.request.TagCreateRequest
import com.example.blogenginekotlin.dto.request.TagUpdateRequest
import com.example.blogenginekotlin.entity.Tag
import com.example.blogenginekotlin.repository.TagRepository
import com.example.blogenginekotlin.service.TagService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TagServiceImpl(
    private val tagRepository: TagRepository
) : TagService {

    @Transactional
    override fun createTag(request: TagCreateRequest): TagDto {
        if (tagRepository.existsByName(request.name)) {
            throw IllegalArgumentException("Tag with name '${request.name}' already exists")
        }

        val tag = Tag(
            name = request.name,
            description = request.description
        )

        val savedTag = tagRepository.save(tag)
        return TagDto.from(savedTag)
    }

    override fun getTagById(id: Long): TagDto {
        val tag = tagRepository.findById(id)
            .orElseThrow { NoSuchElementException("Tag not found with id: $id") }
        return TagDto.from(tag)
    }

    override fun getTagByName(name: String): TagDto {
        val tag = tagRepository.findByName(name)
            ?: throw NoSuchElementException("Tag not found with name: $name")
        return TagDto.from(tag)
    }

    override fun getTagBySlug(slug: String): TagDto {
        val tag = tagRepository.findBySlug(slug)
            ?: throw NoSuchElementException("Tag not found with slug: $slug")
        return TagDto.from(tag)
    }

    @Transactional
    override fun updateTag(id: Long, request: TagUpdateRequest): TagDto {
        val tag = tagRepository.findById(id)
            .orElseThrow { NoSuchElementException("Tag not found with id: $id") }

        request.name?.let {
            if (it != tag.name && tagRepository.existsByName(it)) {
                throw IllegalArgumentException("Tag with name '$it' already exists")
            }
            tag.name = it
        }

        request.description?.let {
            tag.description = it
        }

        val updatedTag = tagRepository.save(tag)
        return TagDto.from(updatedTag)
    }

    @Transactional
    override fun deleteTag(id: Long) {
        if (!tagRepository.existsById(id)) {
            throw NoSuchElementException("Tag not found with id: $id")
        }
        
        tagRepository.deleteById(id)
    }

    override fun getAllTags(pageable: Pageable): Page<TagDto> {
        return tagRepository.findAll(pageable).map { TagDto.from(it) }
    }

    override fun getPopularTags(limit: Int): List<TagDto> {
        val pageable = PageRequest.of(0, limit)
        return tagRepository.findPopularTags(pageable).map { TagDto.from(it) }
    }

    override fun searchTags(keyword: String, pageable: Pageable): Page<TagDto> {
        return tagRepository.searchByKeyword(keyword, pageable).map { TagDto.from(it) }
    }
}