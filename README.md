# E-Commerce Android App with Claude AI Chatbot

An Android e-commerce application built with Java and SQLite, featuring an AI-powered customer support chatbot using Claude (Anthropic API).

## Features

- Product browsing by categories
- Product search functionality
- Product details with images, price, and description
- Shopping cart management
- Order placement and tracking
- User profile management
- Claude AI-powered chatbot for customer support

## Technical Stack

- **Language**: Java
- **Database**: SQLite
- **UI Components**: AndroidX, Material Design
- **Navigation**: Navigation Component
- **Image Loading**: Glide
- **Networking**: Retrofit, OkHttp
- **AI Integration**: Anthropic Claude API

## Project Structure

The application is organized using the Fragment-based architecture with RecyclerView adapters for displaying lists:

- **Models**: Product, Category, CartItem, Order, User, ChatMessage
- **Adapters**: ProductAdapter, CategoryAdapter, CartAdapter, OrderAdapter, ChatAdapter
- **Fragments**: HomeFragment, ProductDetailFragment, CartFragment, CheckoutFragment, OrderConfirmationFragment, OrdersFragment, ProfileFragment, ChatbotFragment
- **Database**: DatabaseHelper for SQLite operations
- **API**: AnthropicClient for Claude API integration

## Claude API Integration

The chatbot feature uses Anthropic's Claude API to provide intelligent customer support. To use this feature:

1. Obtain an API key from Anthropic (https://console.anthropic.com/)
2. Set the API key as an environment variable: `ANTHROPIC_API_KEY=your_api_key`
3. The app will automatically use this key for all chatbot conversations

## Running the Claude API Demo

For demonstration purposes, a standalone Claude API client is included:

```
javac ClaudeApiDemo.java
java ClaudeApiDemo
```

This will simulate a customer conversation with the chatbot, using the same API integration that the Android app uses.

## Building the Android App

To build the application:

```
./gradlew assembleDebug
```

The APK file will be generated in the `app/build/outputs/apk/debug/` directory.

## Testing on Android Devices

You can install the generated APK file on an Android device or emulator to test the application.

## Notes

- This app is designed for Android API 21 (Lollipop) and above
- For best performance, use on devices with Android 8.0 or higher
- The chatbot requires internet connectivity to function