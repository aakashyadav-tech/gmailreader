# Gmail Reader Application

This application uses the Gmail API to read your Gmail messages. It is a Spring Boot app packaged as a JAR.

---

## 🔑 Setup Gmail API and Credentials

To use this application, you need to enable the Gmail API and generate OAuth 2.0 credentials.

### Step 1: Enable Gmail API

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. In the **Navigation menu**, go to **APIs & Services > Library**.
4. Search for **Gmail API** and click **Enable**.

### Step 2: Create OAuth 2.0 Credentials

1. Go to **APIs & Services > Credentials**.
2. Click **Create Credentials** and select **OAuth client ID**.
3. Configure the consent screen if prompted (fill in required fields).
4. Choose **Desktop app** as the application type.
5. Give it a name and click **Create**.
6. Download the generated JSON file (`credentials.json`).
7. Place credential.json on your preferred place.
---

## 🚀 Launch the Application

1. Open a terminal or command prompt.
2. Navigate to the `executable/` folder containing `gmailreader.jar`.

### Follow below command to launch on Windows OS
```bash
cd executable
set google-credentials=/path/to/credentials/credential.json && java -jar gmailreader.jar
```
### For linux run below command
```bash
cd executable && google-credentials=/path/to/credentials/credential.json java -jar gmailreader.jar
```
