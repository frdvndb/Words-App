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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptor untuk [RecyclerView] di [DetailActivity].
 */
class WordAdapter(private val letterId: String, context: Context) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private val filteredWords: List<String>

    init {
        // Ambil daftar kata dari res/values/arrays.xml
        val words = context.resources.getStringArray(R.array.words).toList()

        filteredWords = words
            // Mengembalikan item dalam koleksi jika kondisional benar,
            // dalam hal ini jika item di mulai dengan huruf yang diberikan,
            // abaikan huruf besar atau kecil.
            .filter { it.startsWith(letterId, ignoreCase = true) }
            // Mengembalikan koleksi yang telah diacak di tempatnya
            .shuffled()
            // Mengembalikan n item pertama sebagai [List]
            .take(5)
            // Mengembalikan versi terurut dari [List]
            .sorted()
    }

    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }

    override fun getItemCount(): Int = filteredWords.size

    /**
     * Membuat tampilan baru dengan R.layout.item_view sebagai template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        // Siapkan delegasi aksesibilitas khusus untuk menyetel pembacaan teks
        layout.accessibilityDelegate = Accessibility

        return WordViewHolder(layout)
    }

    /**
     * Mengganti konten tampilan yang ada dengan data baru
     */
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = filteredWords[position]
        // Diperlukan untuk memanggil startActivity
        val context = holder.view.context
        // Tetapkan teks WordViewHolder
        holder.button.text = item
        // Menetapkan [OnClickListener] ke tombol yang ada di [ViewHolder]
        holder.button.setOnClickListener {
            val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}${item}")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context.startActivity(intent)
        }

    }
    // Siapkan delegasi aksesibilitas khusus untuk menyetel
    // teks yang dibaca dengan layanan aksesibilitas
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
            val customString = host.context?.getString(R.string.look_up_word)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info.addAction(customClick)
        }
    }
}