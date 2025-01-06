package com.example.mobilnepwr.data.images

import com.example.mobilnepwr.data.notes.Note
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDao: ImageDao) {
    fun getAllItemsStream(): Flow<List<Image>> = imageDao.getAllImages()
    fun getItemStream(id: Int): Flow<Image?> =imageDao.getImage(id)


    suspend fun insertItem(item: Image) = imageDao.insertImage(item)

    suspend fun deleteItem(item: Image) = imageDao.deleteImage(item)

    suspend fun updateItem(item: Image) = imageDao.updateImage(item)
    //fun getImageByCourseId(courseId: Int): Flow<List<Image>> =
    //    ImageDao.getImageByCourseId(courseId)

}