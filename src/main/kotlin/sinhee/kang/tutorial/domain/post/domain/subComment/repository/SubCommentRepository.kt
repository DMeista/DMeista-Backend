package sinhee.kang.tutorial.domain.post.domain.subComment.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment

@Repository
interface SubCommentRepository : CrudRepository<SubComment, Int>
