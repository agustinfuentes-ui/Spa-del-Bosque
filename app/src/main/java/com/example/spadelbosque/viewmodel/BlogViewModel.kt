package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.model.BlogArticulo
import com.example.spadelbosque.repository.BlogRepository

class BlogViewModel : ViewModel(){
    fun getArticles(): List<BlogArticulo>{
        return BlogRepository.getArticles()
    }
}