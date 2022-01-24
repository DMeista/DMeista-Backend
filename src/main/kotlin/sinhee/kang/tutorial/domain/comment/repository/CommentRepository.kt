package sinhee.kang.tutorial.domain.comment.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.comment.entity.Comment

@Repository
interface CommentRepository : CrudRepository<Comment, Int>
