package sinhee.kang.tutorial.domain.post.repository.subComment

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.post.entity.subComment.SubComment

@Repository
interface SubCommentRepository : CrudRepository<SubComment, Int>
