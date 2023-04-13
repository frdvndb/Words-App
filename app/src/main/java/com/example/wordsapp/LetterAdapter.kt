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

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptor untuk [RecyclerView] di [MainActivity].
 */
class LetterAdapter :
    RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    // Menghasilkan jangkauan char dari 'A' ke 'Z' dan mengubahnya menjadi daftar
    private val list = ('A').rangeTo('Z').toList()

    /**
     * Memberikan referensi untuk tampilan yang diperlukan
     * untuk menampilkan item dalam daftar.
     */
    class LetterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Membuat tampilan baru dengan R.layout.item_view sebagai template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_view, parent, false)
        // Siapkan delegasi aksesibilitas khusus untuk menyetel pembacaan teks
        layout.accessibilityDelegate = Accessibility
        return LetterViewHolder(layout)
    }

    /**
     * Mengganti konten tampilan yang ada dengan data baru
     */
    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val item = list.get(position)
        holder.button.text = item.toString()

        // Menetapkan [OnClickListener] ke tombol yang ada di [ViewHolder]
        holder.button.setOnClickListener {
            val context = holder.view.context
            // Buat maksud dengan tujuan DetailActivity
            val intent = Intent(context, DetailActivity::class.java)
            // Tambahkan huruf yang dipilih ke maksud sebagai data tambahan
            // Teks Tombol adalah [CharSequence], daftar karakter,
            // sehingga harus secara eksplisit diubah menjadi [String].
            intent.putExtra(DetailActivity.LETTER, holder.button.text.toString())
            // Memulai aktivitas menggunakan data dan tujuan dari Intent.
            context.startActivity(intent)
        }
    }

    // Siapkan delegasi aksesibilitas khusus dengan
    // layanan aksesibilitas untuk menyetel teks yang dibaca
    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View,
            info: AccessibilityNodeInfo
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // Dengan `null` sebagai argumen kedua untuk [AccessibilityAction],
            // layanan aksesibilitas mengumumkan "ketuk dua kali untuk mengaktifkan".
            // Jika string khusus disediakan,
            // itu mengumumkan "ketuk dua kali ke <custom string>".
            val customString = host.context?.getString(R.string.look_up_words)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info.addAction(customClick)
        }
    }
}