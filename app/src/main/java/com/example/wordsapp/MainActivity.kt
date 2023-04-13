/*
 * Copyright (C) 2020 The Android Open Source Project
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
package com.example.wordsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.ActivityMainBinding

/**
 * Aktivitas Utama dan titik masuk untuk aplikasi. Menampilkan RecyclerView huruf.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    // Melacak LayoutManager mana yang digunakan untuk [RecyclerView]
    private var isLinearLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView

        // Menyetel LinearLayoutManager dari recyclerview
        chooseLayout()
    }

    /**
     * Menyetel LayoutManager untuk [RecyclerView] berdasarkan orientasi daftar yang diinginkan.
     */
    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerView.adapter = LetterAdapter()
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        // Setel drawable untuk ikon menu berdasarkan LayoutManager yang saat ini digunakan
        // Pengkondisian untuk menentukan icon yang ditampilkan untuk menuItem
        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
    }

    /**
     * Memulai [Menu] untuk digunakan dengan [Aktivitas] saat ini
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu?.findItem(R.id.action_switch_layout)
        // Memanggil kode untuk menyetel ikon berdasarkan LinearLayoutManager dari RecyclerView
        setIcon(layoutButton)

        return true
    }

    /**
     * Menentukan cara menangani interaksi dengan [MenuItem] yang dipilih
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Setel isLinearLayoutManager (Boolean) ke nilai yang berlawanan
                isLinearLayoutManager = !isLinearLayoutManager

                // Mengatur tata letak dan ikon
                chooseLayout()
                setIcon(item)

                return true
            }
            // Jika tidak, jangan lakukan apa pun dan gunakan penanganan event inti

            // ketika mengharuskan semua yang mungkin diperhitungkan secara eksplisit,
            // misalnya instance benar dan salah jika nilainya adalah Boolean,
            // atau yang lain untuk menangkap semua kasus yang tidak tertangani.
            else -> super.onOptionsItemSelected(item)
        }
    }
}