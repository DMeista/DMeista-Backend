package sinhee.kang.tutorial.domain.file.domain

import sinhee.kang.tutorial.domain.post.domain.post.Post
import javax.persistence.*

@Entity(name = "tbl_image")
class ImageFile (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var imageId: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    var post: Post,

    var fileName: String
)