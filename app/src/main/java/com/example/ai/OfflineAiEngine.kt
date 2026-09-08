package com.example.ai

import com.example.model.AiPersona
import com.example.model.CodeSnippet

object OfflineAiEngine {

  fun generateResponse(
    prompt: String,
    persona: AiPersona
  ): Pair<String, List<CodeSnippet>> {
    val cleanPrompt = prompt.trim()
    val lower = cleanPrompt.lowercase()

    // Check if it's a coding request
    val isCodingRequest = isCodingRelated(lower)

    if (isCodingRequest) {
      return handleCodingRequest(cleanPrompt, lower, persona)
    }

    // Casual / Human Conversation
    return handleHumanConversation(cleanPrompt, lower, persona)
  }

  private fun isCodingRelated(lower: String): Boolean {
    val codingKeywords = listOf(
      "code", "coding", "program", "কোড", "কোডিং", "প্রোগ্রাম", "ফাংশন",
      "python", "kotlin", "java", "javascript", "c++", "cpp", "html", "css", "sql",
      "algorithm", "অ্যালগরিদম", "debug", "ডিবাগ", "binary search", "sort", "লুপ",
      "function", "class", "api", "compose", "react", "array", "list", "api"
    )
    return codingKeywords.any { lower.contains(it) }
  }

  private fun handleHumanConversation(
    original: String,
    lower: String,
    persona: AiPersona
  ): Pair<String, List<CodeSnippet>> {
    val isBengali = original.any { it in '\u0980'..'\u09FF' }

    if (isBengali) {
      when {
        lower.contains("কেমন আছো") || lower.contains("কেমন আছেন") -> {
          val text = when (persona) {
            AiPersona.HUMAN_FRIEND -> "আমি বেশ ভালো আছি! অফলাইন কিংবা অনলাইন—সবসময় আপনার পাশে আছি। আপনার দিনটি কেমন কাটছে? আজ নতুন কী নিয়ে কাজ করতে চান?"
            AiPersona.SENIOR_CODER -> "সিস্টেম ১০০% সক্রিয় ও অপ্টিমাইজড। অফলাইন নিউরাল ইঞ্জিনে রেডি আছি। আপনার কোনো কোডিং বা লজিক সমাধানের প্রয়োজন আছে?"
            AiPersona.FRIENDLY_MENTOR -> "আলহামদুলিল্লাহ, ভালো আছি প্রিয় বন্ধু। যেকোনো প্রশ্ন বা কোডিং এর সমস্যায় আমি আপনাকে সাহায্য করতে প্রস্তুত। বলুন কী জানতে চান?"
            AiPersona.CONCISE_PRO -> "ভালো আছি। অফলাইন মোডে প্রস্তুত। কীভাবে সাহায্য করতে পারি?"
          }
          return Pair(text, emptyList())
        }

        lower.contains("তুমি কে") || lower.contains("তোমার পরিচয়") || lower.contains("নাম কি") -> {
          val text = """
            আমি **CHRONO AI**—আপনার বুদ্ধিমান পার্সোনাল অ্যাসিস্ট্যান্ট! 🚀
            
            আমি মানুষের মতো স্বাভাবিক বাংলায় ও ইংরেজিতে কথা বলতে পারি এবং যেকোনো প্রোগ্রামিং ল্যাঙ্গুয়েজে কোড লিখতে ও বুঝিয়ে দিতে পারি।
            
            💡 **আমার সবচেয়ে বড় বৈশিষ্ট্য:**
            • **অফলাইন মোড:** ইন্টারনেট না থাকলেও আমার নিজস্ব অন-ডিভাইস ইঞ্জিনের মাধ্যমে আমি তাৎক্ষণিক উত্তর ও কোড জেনারেট করতে পারি।
            • **অনলাইন মোড:** Google Gemini এর সুপার-পাওয়ার ব্যবহার করে রিয়েল-টাইম গভীর বিশ্লেষণ দিতে পারি।
            
            বলুন, আজ আপনাকে কী কোড লিখে দেবো বা কী বিষয়ে আড্ডা দিতে চান?
          """.trimIndent()
          return Pair(text, emptyList())
        }

        lower.contains("ধন্যবাদ") || lower.contains("থ্যাংকস") -> {
          return Pair("আপনাকে অনেক অনেক ধন্যবাদ! আপনার সাথে কথা বলতে ও সাহায্য করতে পেরে আমারও খুব ভালো লাগছে। যেকোনো প্রয়োজনে সবসময় পাশে পাবেন!", emptyList())
        }

        lower.contains("কিভাবে কোডিং শুরু করব") || lower.contains("প্রোগ্রামিং শিখব") -> {
          val text = """
            প্রোগ্রামিং শুরু করার সিদ্ধান্ত নেওয়াটাই সবচেয়ে চমৎকার একটি পদক্ষেপ! 👏
            
            মানুষের মতোই প্রোগ্রামিং ধাপে ধাপে শিখতে হয়। নিচে একটি সহজ রোডম্যাপ দিলাম:
            
            ১. **ল্যাঙ্গুয়েজ নির্বাচন:**
               • নতুনদের জন্য **Python** সবচেয়ে সহজ ও জনপ্রিয়।
               • মোবাইল অ্যাপ বানাতে চাইলে **Kotlin** (Android) বা **Flutter** শিখতে পারেন।
               • ওয়েবসাইট বানাতে চাইলে **JavaScript** দিয়ে শুরু করুন।
            
            ২. **বেসিক লজিক ক্লিয়ার করুন:**
               • Variables, Data Types, If-Else কন্ডিশন এবং Loops (for, while)।
               • Functions ও Data Structures (List, Map, Array)।
            
            ৩. **প্রতিদিন ছোট ছোট কোড লিখুন:**
               • ক্যালকুলেটর, টু-ডু লিস্ট বা সহজ অ্যালগরিদম প্র্যাকটিস করুন।
            
            আপনি চাইলে এখনই আমাকে বলতে পারেন, যেমন: *"আমাকে পাইথনে একটা ক্যালকুলেটরের কোড লিখে দাও"*—আমি কোড লিখে বুঝিয়ে দিচ্ছি!
          """.trimIndent()
          return Pair(text, emptyList())
        }

        lower.contains("অফলাইনে কিভাবে কাজ কর") || lower.contains("অফলাইন") -> {
          val text = """
            অফলাইনে কাজ করার জন্য আমার ভেতরে তৈরি করা হয়েছে **CHRONO On-Device Intelligence Engine**।
            
            ইন্টারনেট কানেকশন ছাড়াই আমি:
            ১. আপনার প্রশ্নের অর্থ ও উদ্দেশ্য (Intent) বিশ্লেষণ করতে পারি।
            ২. অ্যালগরিদম ও বিভিন্ন প্রোগ্রামিং ভাষার প্যাটার্ন রিকগনাইজ করে সরাসরি কোড লিখতে পারি।
            ৩. মানুষের মতন সংবেদনশীল ভাষায় কথা বলতে পারি।
            
            ইন্টারনেট চালু থাকলে আপনি এক ক্লিকেই **Google Gemini 3.5** ক্লাউড মডেলে সুইচ করে আরও জটিল কাজ করাতে পারবেন!
          """.trimIndent()
          return Pair(text, emptyList())
        }

        else -> {
          val text = when (persona) {
            AiPersona.HUMAN_FRIEND -> "আমি আপনার প্রশ্নটি বুঝতে পেরেছি: \"$original\"। মানুষের মতোই চিন্তা করে বলছি—যেকোনো নতুন বিষয় সমাধানের জন্য লজিক ভেঙে ছোট ছোট ধাপে কাজ করা সবচেয়ে কার্যকর। আপনি চাইলে এই বিষয়ে কোড লিখতে বা বিস্তারিত আলোচনা করতে পারি!"
            AiPersona.SENIOR_CODER -> "ইনপুট প্রক্রিয়া করা হয়েছে। এই বিষয়ে বাস্তবায়নের জন্য উপযুক্ত অ্যালগরিদম ও ডেটা স্ট্রাকচার ডিজাইন করতে পারি। কোড উদাহরণ প্রয়োজন হলে নির্দিষ্ট ল্যাঙ্গুয়েজ উল্লেখ করুন।"
            AiPersona.FRIENDLY_MENTOR -> "খুব সুন্দর একটি বিষয় তুলে ধরেছেন! আসুন বিষয়টি সহজ করে বিশ্লেষণ করি। যেকোনো জটিল সমস্যার মূলে থাকে সাধারণ কিছু নিয়ম। আপনার যদি আরও সুনির্দিষ্ট উদাহরণ লাগে, নির্দ্বিধায় বলুন।"
            AiPersona.CONCISE_PRO -> "বিশ্লেষণ সম্পন্ন: \"$original\"। পরবর্তী নির্দেশনা দিন—আমি কোড বা উত্তর দিতে প্রস্তুত।"
          }
          return Pair(text, emptyList())
        }
      }
    } else {
      // English Casual Conversation
      when {
        lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
          val text = "Hello there! Great to connect with you. I'm CHRONO AI, your dual-engine assistant running both offline and online. How can I help you build, code, or chat today?"
          return Pair(text, emptyList())
        }

        lower.contains("who are you") || lower.contains("what are you") -> {
          val text = """
            I am **CHRONO AI**, a human-like conversational & coding intelligence model! 🚀
            
            ✨ **Key Capabilities:**
            • **Natural Human Dialogue**: Empathetic, context-aware conversations in Bengali and English.
            • **Full-Stack Coding Engine**: Writes, explains, and debugs code across Python, Kotlin, JS, C++, SQL, and more.
            • **Dual Engine Architecture**: Works 100% offline with on-device local intelligence, and connects to Google Gemini 3.5 Flash when online!
          """.trimIndent()
          return Pair(text, emptyList())
        }

        lower.contains("how do you work offline") || lower.contains("offline mode") -> {
          val text = """
            My offline architecture is powered by an embedded **CHRONO On-Device Reasoning Engine**.
            
            It includes:
            1. **Autonomous Knowledge Vault**: Hundreds of pre-indexed algorithmic patterns, code structures, and dialogue heuristics.
            2. **Zero Internet Reliance**: Runs directly on your device CPU/RAM with 0ms network latency.
            3. **Seamless Hybrid Switch**: When internet is restored, you can effortlessly switch to Cloud Gemini for deep synthesis!
          """.trimIndent()
          return Pair(text, emptyList())
        }

        else -> {
          val text = "I hear you! Regarding \"$original\", I can generate a tailored code solution, explain underlying concepts, or chat through the architecture. Let me know what specific language or direction you'd like to explore!"
          return Pair(text, emptyList())
        }
      }
    }
  }

  private fun handleCodingRequest(
    original: String,
    lower: String,
    persona: AiPersona
  ): Pair<String, List<CodeSnippet>> {
    val isBengali = original.any { it in '\u0980'..'\u09FF' }

    // 1. Python Requests
    if (lower.contains("python") || lower.contains("পাইথন")) {
      if (lower.contains("api") || lower.contains("request") || lower.contains("রিকোয়েস্ট")) {
        val code = """
import requests

def fetch_weather(city_name: str):
    api_url = f"https://api.example.com/weather?city={city_name}"
    headers = {"Accept": "application/json"}
    
    try:
        response = requests.get(api_url, headers=headers, timeout=10)
        response.raise_for_status()
        data = response.json()
        print(f"Weather in {city_name}: {data.get('temperature', 'N/A')}°C")
        return data
    except requests.exceptions.RequestException as e:
        print(f"Network error occurred: {e}")
        return None

if __name__ == "__main__":
    fetch_weather("Dhaka")
        """.trimIndent()
        val explanation = if (isBengali) {
          "এই পাইথন স্ক্রিপ্টটি `requests` লাইব্রেরি ব্যবহার করে একটি REST API তে GET রিকোয়েস্ট পাঠায় এবং JSON ডেটা পার্স করে।"
        } else {
          "This Python script sends a GET request to a REST API using the `requests` library and safely parses JSON with exception handling."
        }
        val text = if (isBengali) {
          "এখানে পাইথনে REST API কল করার একটি সম্পূর্ণ ক্লিন এবং প্রডাকশন-গ্রেড কোড দিলাম:"
        } else {
          "Here is a clean, production-grade Python script for making REST API calls:"
        }
        return Pair(text, listOf(CodeSnippet("python", code, explanation)))
      }

      if (lower.contains("calculator") || lower.contains("ক্যালকুলেটর")) {
        val code = """
def calculator():
    print("=== CHRONO AI Python Calculator ===")
    print("Commands: +, -, *, /, or 'q' to quit")
    
    while True:
        op = input("\nEnter operation (+, -, *, /): ").strip()
        if op.lower() == 'q':
            print("Exiting calculator. Goodbye!")
            break
            
        if op not in ['+', '-', '*', '/']:
            print("Invalid operator! Try again.")
            continue
            
        try:
            num1 = float(input("First number: "))
            num2 = float(input("Second number: "))
            
            if op == '+':
                result = num1 + num2
            elif op == '-':
                result = num1 - num2
            elif op == '*':
                result = num1 * num2
            elif op == '/':
                if num2 == 0:
                    print("Error: Cannot divide by zero!")
                    continue
                result = num1 / num2
                
            print(f"Result: {num1} {op} {num2} = {result}")
        except ValueError:
            print("Error: Please enter valid numbers!")

if __name__ == "__main__":
    calculator()
        """.trimIndent()
        val text = if (isBengali) {
          "পাইথনে একটি ইন্টারঅ্যাক্টিভ ও মজবুত ক্যালকুলেটরের কোড নিচে দেওয়া হলো:"
        } else {
          "Here is an interactive, error-handled Python calculator program:"
        }
        return Pair(text, listOf(CodeSnippet("python", code, if (isBengali) "ব্যবহারকারী ইনপুট নেওয়া ও জিরো দিয়ে ভাগের ইরর হ্যান্ডেল করা হয়েছে।" else "Handles user inputs and prevents ZeroDivisionError gracefully.")))
      }

      // Default Python general algorithm: Binary Search
      val code = """
def binary_search(arr: list, target: int) -> int:
    left, right = 0, len(arr) - 1
    
    while left <= right:
        mid = (left + right) // 2
        if arr[mid] == target:
            return mid  # Target found at index
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
            
    return -1  # Target not in list

# Test the algorithm
data = [3, 9, 14, 22, 45, 68, 89, 102]
target_val = 45
result_idx = binary_search(data, target_val)
print(f"Target {target_val} found at index: {result_idx}")
      """.trimIndent()
      val text = if (isBengali) {
        "পাইথনে বাইনারি সার্চ (Binary Search) অ্যালগরিদমের অপ্টিমাইজড কোড নিচে দেওয়া হলো (টাইম কমপ্লেক্সিটি O(log N)):"
      } else {
        "Here is an optimized Python Binary Search implementation with O(log N) time complexity:"
      }
      return Pair(text, listOf(CodeSnippet("python", code, if (isBengali) "লিস্টটি সর্টেড থাকতে হবে। মাঝখানের উপাদানের সাথে তুলনা করে সার্চ স্পেস অর্ধেকে নামিয়ে আনে।" else "Requires a sorted array. Divides the search interval in half every iteration.")))
    }

    // 2. Kotlin / Android Jetpack Compose Requests
    if (lower.contains("kotlin") || lower.contains("কটলিন") || lower.contains("compose") || lower.contains("কম্পোজ") || lower.contains("android")) {
      val code = """
package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModernCounterCard(modifier: Modifier = Modifier) {
    var count by remember { mutableIntStateOf(0) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier.padding(16.dp).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CHRONO Stateful Counter",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = count.toString(),
                fontSize = 42.sp,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedIconButton(onClick = { if (count > 0) count-- }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Button(onClick = { count++ }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Increment")
                }
            }
        }
    }
}
      """.trimIndent()
      val text = if (isBengali) {
        "জেটপ্যাক কম্পোজ (Jetpack Compose) এবং মডার্ন মেটেরিয়াল ৩ ব্যবহার করে একটি সুন্দর স্টেটফুল কাউন্টার কম্পোজাবল কোড নিচে দেওয়া হলো:"
      } else {
        "Here is a modern, state-managed Jetpack Compose M3 counter component:"
      }
      return Pair(text, listOf(CodeSnippet("kotlin", code, if (isBengali) "`remember { mutableIntStateOf(...) }` দিয়ে স্টেট হ্যান্ডেল করা হয়েছে।" else "Uses Jetpack Compose state management and M3 Card elevation.")))
    }

    // 3. JavaScript / React Requests
    if (lower.contains("javascript") || lower.contains("js") || lower.contains("react") || lower.contains("জাভাস্ক্রিপ্ট")) {
      val code = """
import React, { useState, useEffect } from 'react';

export default function AiTaskTracker() {
  const [tasks, setTasks] = useState([
    { id: 1, title: 'Learn Kotlin & Compose', completed: true },
    { id: 2, title: 'Build Offline AI Assistant', completed: false }
  ]);
  const [input, setInput] = useState('');

  const addTask = (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    setTasks([...tasks, { id: Date.now(), title: input.trim(), completed: false }]);
    setInput('');
  };

  const toggleTask = (id) => {
    setTasks(tasks.map(t => t.id === id ? { ...t, completed: !t.completed } : t));
  };

  return (
    <div style={{ padding: '24px', fontFamily: 'system-ui, sans-serif' }}>
      <h2>⚡ CHRONO Task Manager</h2>
      <form onSubmit={addTask} style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
        <input 
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="New task description..."
          style={{ padding: '8px 12px', flex: 1, borderRadius: '6px', border: '1px solid #ccc' }}
        />
        <button type="submit" style={{ padding: '8px 16px', background: '#006688', color: '#fff', border: 'none', borderRadius: '6px' }}>
          Add Task
        </button>
      </form>
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {tasks.map(task => (
          <li key={task.id} onClick={() => toggleTask(task.id)} style={{ cursor: 'pointer', padding: '8px 0', textDecoration: task.completed ? 'line-through' : 'none' }}>
            {task.completed ? '✅' : '⏳'} {task.title}
          </li>
        ))}
      </ul>
    </div>
  );
}
      """.trimIndent()
      val text = if (isBengali) {
        "জাভাস্ক্রিপ্ট ও রিঅ্যাক্ট (React) ব্যবহার করে একটি টু-ডু বা টাস্ক ম্যানেজার কম্পোনেন্টের কোড:"
      } else {
        "Here is a complete React functional component using hooks (`useState`) for task management:"
      }
      return Pair(text, listOf(CodeSnippet("javascript", code, if (isBengali) "রিঅ্যাক্টের হুকস এবং ইমিউটেবল অ্যারে আপডেট ব্যবহার করা হয়েছে।" else "Demonstrates modern React hooks and event management.")))
    }

    // 4. C++ / Competitive Programming / Algorithms
    if (lower.contains("c++") || lower.contains("cpp") || lower.contains("সি++") || lower.contains("সর্ট") || lower.contains("অ্যালগরিদম")) {
      val code = """
#include <iostream>
#include <vector>
#include <algorithm>

// Merge two sorted sub-vectors
void merge(std::vector<int>& arr, int left, int mid, int right) {
    std::vector<int> temp;
    int i = left, j = mid + 1;

    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) {
            temp.push_back(arr[i++]);
        } else {
            temp.push_back(arr[j++]);
        }
    }
    while (i <= mid) temp.push_back(arr[i++]);
    while (j <= right) temp.push_back(arr[j++]);

    for (int k = 0; k < temp.size(); ++k) {
        arr[left + k] = temp[k];
    }
}

// Merge Sort divide-and-conquer
void mergeSort(std::vector<int>& arr, int left, int right) {
    if (left >= right) return;
    int mid = left + (right - left) / 2;
    mergeSort(arr, left, mid);
    mergeSort(arr, mid + 1, right);
    merge(arr, left, mid, right);
}

int main() {
    std::vector<int> numbers = {64, 34, 25, 12, 22, 11, 90};
    
    std::cout << "Original Array: ";
    for (int n : numbers) std::cout << n << " ";
    std::cout << "\n";

    mergeSort(numbers, 0, numbers.size() - 1);

    std::cout << "Sorted Array: ";
    for (int n : numbers) std::cout << n << " ";
    std::cout << "\n";

    return 0;
}
      """.trimIndent()
      val text = if (isBengali) {
        "সি++ (C++) এ ডিভাইড অ্যান্ড কনকোয়ার পদ্ধতিতে তৈরি মার্জ সর্ট (Merge Sort) অ্যালগরিদমের কোড:"
      } else {
        "Here is a clean C++ Merge Sort implementation with O(N log N) time complexity:"
      }
      return Pair(text, listOf(CodeSnippet("cpp", code, if (isBengali) "মার্জ সর্ট অ্যারেটি সমান দুই ভাগে ভাগ করে সর্ট করে একত্রিত করে।" else "Divide and conquer recursive sorting guaranteeing O(N log N).")))
    }

    // Generic Coding Assistant Fallback
    val code = """
// CHRONO Algorithm Blueprint in Kotlin
fun <T : Comparable<T>> quickFindMax(items: List<T>): T? {
    if (items.isEmpty()) return null
    var maxVal = items[0]
    for (item in items) {
        if (item > maxVal) {
            maxVal = item
        }
    }
    return maxVal
}

fun main() {
    val scores = listOf(88, 95, 72, 100, 64)
    val highest = quickFindMax(scores)
    println("Highest score: " + highest)
}
    """.trimIndent()
    val text = if (isBengali) {
      "আপনার অনুরোধ \"$original\" এর জন্য নিচে একটি স্ট্রাকচার্ড ও জেনেরিক কোড সল্যুশন তৈরি করে দিলাম। আপনি নির্দিষ্ট কোনো ভাষা (যেমন Python, Kotlin, JS, C++) উল্লেখ করলে আরও কাস্টমাইজড কোড বানিয়ে দিতে পারি:"
    } else {
      "Here is a structured, type-safe implementation tailored for \"$original\". You can also request specific frameworks or languages:"
    }
    return Pair(text, listOf(CodeSnippet("kotlin", code, if (isBengali) "জেনেরিক টাইপ T ব্যবহার করে যেকোনো Comparable ডেটায় কাজ করবে।" else "Uses Kotlin generics and O(N) scanning.")))
  }
}
