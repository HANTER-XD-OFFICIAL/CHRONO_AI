# 🚀 CHRONO AI - APK Build & GitHub Actions Guide

এই রিপোজিটরিতে সম্পূর্ণ অটোমেটিক **GitHub Actions CI/CD Workflows** কনফিগার করা আছে। আপনি কোনো ধরণের জটিলতা ছাড়াই সরাসরি GitHub থেকে APK বিল্ড করে মোবাইলে ডাউনলোড করতে পারবেন।

---

## 📱 পদ্ধতি ১: GitHub Actions থেকে ১-ক্লিকে APK ডাউনলোড (সবচেয়ে সহজ)

১. আপনার রিপোজিটরিতে গিয়ে উপরের মেনু থেকে **"Actions"** ট্যাবে ক্লিক করুন।
২. বাম পাশের লিস্ট থেকে **"Build Android APK (CHRONO AI)"** সিলেক্ট করুন।
৩. ডানপাশের **"Run workflow"** বাটনে ক্লিক করুন।
৪. Build Type (`debug`, `release`, বা `both`) সিলেক্ট করে সবুজ **"Run workflow"** বাটনে চাপুন।
৫. ২-৩ মিনিটের মধ্যে বিল্ড সম্পন্ন হলে রানের বিস্তারিত পেজে নিচে **"Artifacts"** সেকশন দেখতে পাবেন।
৬. সেখানে থাকা **`CHRONO-AI-APK`**-এ ক্লিক করলেই জিপ ফাইল আকারে আপনার ইন্সটলেবল APK ডাউনলোড হয়ে যাবে!

---

## 🏷️ পদ্ধতি ২: নতুন রিলিজ ও অটোমেটিক APK তৈরি (Tag Release)

আপনি যখনই গিটহাবে কোনো নতুন ট্যাগ পুশ করবেন:
```bash
git tag v1.0.0
git push origin v1.0.0
```
স্বয়ংক্রিয়ভাবে `.github/workflows/release-tag.yml` রান হবে এবং আপনার রিপোজিটরির **Releases** সেকশনে সরাসরি ডাউনলোডযোগ্য `CHRONO-AI-v1.0.0.apk` ফাইলটি যুক্ত হয়ে যাবে।

---

## 💻 পদ্ধতি ৩: লোকাল মেশিনে কমান্ড দিয়ে APK তৈরি

আপনার লোকাল কম্পিউটারে টার্মিনাল ওপেন করে নিচের কমান্ডটি দিন:

### Linux / Mac:
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### Windows (PowerShell / CMD):
```cmd
gradlew.bat assembleDebug
```

বিল্ড সম্পন্ন হলে তৈরি হওয়া APK ফাইলটি পাবেন:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🔑 সিক্রেটস কনফিগারেশন (ঐচ্ছিক)
- আপনার রিপোজিটরির **Settings > Secrets and variables > Actions** এ গিয়ে `GEMINI_API_KEY` অ্যাড করতে পারেন।
- সিক্রেট কী না দিলেও অ্যাপটি স্বয়ংক্রিয়ভাবে নিজস্ব **On-Device Offline Engine** দিয়ে সম্পূর্ণ সচল থাকবে।
