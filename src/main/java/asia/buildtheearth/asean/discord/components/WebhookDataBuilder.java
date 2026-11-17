package asia.buildtheearth.asean.discord.components;

import asia.buildtheearth.asean.discord.components.api.ComponentV2;
import github.scarsz.discordsrv.dependencies.commons.lang3.StringUtils;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.interactions.components.ActionRow;
import github.scarsz.discordsrv.dependencies.jda.api.utils.data.DataArray;
import github.scarsz.discordsrv.dependencies.jda.api.utils.data.DataObject;
import github.scarsz.discordsrv.dependencies.jda.api.utils.data.SerializableData;
import github.scarsz.discordsrv.dependencies.jda.internal.utils.AllowedMentionsImpl;

import github.scarsz.discordsrv.dependencies.jda.internal.utils.BufferedRequestBody;
import github.scarsz.discordsrv.dependencies.okhttp3.MediaType;
import github.scarsz.discordsrv.dependencies.okhttp3.MultipartBody;
import github.scarsz.discordsrv.dependencies.okhttp3.RequestBody;
import github.scarsz.discordsrv.dependencies.okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A builder for creating {@link WebhookData} objects, which represent payloads for sending messages via Discord webhooks.
 * <p>
 * Supports thread name, username, avatar URL, message content, embeds, and interaction components.
 * Also extends {@link AllowedMentionsImpl}, for allowed mentions settings.
 * </p>
 *
 * <p>usage:</p>
 * <blockquote>{@snippet :
 * WebhookData data = new WebhookDataBuilder()
 *     .setContent("Hello, Discord!")
 *     .suppressNotifications(true) // Suppresses notifications
 *     .build();
 * }</blockquote>
 *
 * <p><b>Note:</b> File attachments <i>cannot</i> be set through this builder. To include files,
 * use {@link WebhookData#addFile(File)} on the built {@link WebhookData} instance before preparing the request body.</p>
 */
public class WebhookDataBuilder extends AllowedMentionsImpl {

    private @Nullable String threadName = null;
    private @Nullable String username = null;
    private @Nullable String webhookAvatarUrl = null;
    private @Nullable String content = null;
    private @Nullable Collection<? extends MessageEmbed> embeds = null;
    private @Nullable Collection<? extends ActionRow> components = null;
    private @Nullable Collection<? extends DataObject> componentsV2 = null;
    private @Nullable Integer flags = null;
    private boolean suppressMentions = false;
    private boolean forceComponentV2 = false;

    /**
     * Constructs a new {@link WebhookDataBuilder} instance with default (null/empty) values.
     * <p>
     * Use the setter methods to configure the desired webhook message options, then call {@link #build()}
     * to produce an immutable {@link WebhookData} object ready for dispatch via the Discord API.
     * </p>
     */
    public WebhookDataBuilder() {
        super();
    }

    /**
     * Sets the avatar URL or data URI for the webhook.
     * If using a data URI, it must be valid image data (e.g., {@code data:image/png;base64,...}).
     * Supported image types are PNG, JPEG, and GIF. It's recommended to keep the data URI under 1 MB.
     *
     * @param webhookAvatarUrl A URL or data URI to override the default avatar of the webhook.
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setWebhookAvatarUrl(@Nullable String webhookAvatarUrl) {
        this.webhookAvatarUrl = webhookAvatarUrl;
        return this;
    }

    /**
     * Set the thread name if this webhook data is for executing a new thread on a channel.
     * @param threadName The thread name
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setThreadName(@Nullable String threadName) {
        this.threadName = threadName;
        return this;
    }

    /**
     * Override default username of the webhook
     * @param username A username that follows discord API guidelines
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setUsername(@Nullable String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the message content of the webhook.
     * The content is a string of up to 2000 characters.
     *
     * @param content The message contents of the webhook.
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setContent(@Nullable String content) {
        this.content = content;
        return this;
    }

    /**
     * Sets the embedded rich content for the webhook message.
     * You can provide up to 10 embeds.
     *
     * @param embeds A collection of embedded rich content (up to 10 embed objects).
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setEmbeds(@Nullable Collection<? extends MessageEmbed> embeds) {
        this.embeds = embeds;
        return this;
    }

    /**
     * Sets the interactive message components to include with the message.
     * These could include buttons or other types of UI components.
     *
     * @param components A collection of message components (e.g., buttons).
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setComponents(@Nullable Collection<? extends ActionRow> components) {
        this.components = components;
        return this;
    }

    /**
     * Sets the message components as the new {@link ComponentV2} feature.
     * <p>{@link #forceComponentV2()} must be set for using this method.</p>
     *
     * @param componentsV2  A collection of message components that must inherit {@link ComponentV2}
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder setComponentsV2(@Nullable Collection<? extends ComponentV2> componentsV2) {
        this.componentsV2 = componentsV2;
        return this;
    }

    /**
     * Sets the flags for the message to be {@code SUPPRESS_NOTIFICATIONS},
     * This flag disables push and desktop notifications for the message.
     *
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder suppressNotifications() {
        if(this.flags != null)
            this.flags = flags | (1 << 12);
        else this.flags = (1 << 12);
        return this;
    }

    /**
     * Force {@link ComponentV2} for this webhook data.
     *
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder forceComponentV2() {
        this.forceComponentV2 = true;
        if(this.flags != null)
            this.flags = flags | (1 << 15);
        else this.flags = (1 << 15);
        return this;
    }

    /**
     * Suppress all possible mentions in the data.
     * This will send a {@code allowed_mentions} payload of empty {@code parse} data.
     * @return This builder instance for chaining.
     */
    public WebhookDataBuilder suppressMentions() {
        this.suppressMentions = true;
        return this;
    }

    /**
     * Builds the {@link WebhookData} object with the current builder state.
     *
     * @return The constructed WebhookData object.
     */
    public WebhookData build() {
        // Parse JSON Parameters
        DataObject webhookData = DataObject.empty();

        // Set Thread Name
        if(threadName != null) {
            webhookData.put("thread_name", threadName);
        }

        // Override webhook username
        if(username != null) {
            webhookData.put("username", username);
        }

        // Override avatar image
        if(webhookAvatarUrl != null) {
            webhookData.put("avatar_url", webhookAvatarUrl);
        }

        // Content message
        if (StringUtils.isNotBlank(content)) {
            webhookData.put("content", content);
        }

        // Embeds
        if (embeds != null && !forceComponentV2) {
            DataArray embedArray = DataArray.empty();
            for (MessageEmbed embed : embeds) {
                if (embed != null) {
                    embedArray.add(embed.toData());
                }
            }

            webhookData.put("embeds", embedArray);
        }

        // Components
        if (components != null || componentsV2 != null) {
            DataArray componentsArray = DataArray.empty();

            if(forceComponentV2 && componentsV2 != null) {
                for (DataObject component : componentsV2)
                    componentsArray.add(component.toData());
            }
            else if(components != null) {
                for (ActionRow actionRow : components)
                    componentsArray.add(actionRow.toData());
            }

            webhookData.put("components", componentsArray);
        }

        // Flags
        if(flags != null) {
            webhookData.put("flags", flags);
        }

        // Allow Mentions
        if(!suppressMentions) {
            AllowedMentionsImpl allowedMentionsImpl = this;
            DataObject allowedMentions = allowedMentionsImpl.toData();
            webhookData.put("allowed_mentions", allowedMentions);
        }
        else {
            // Suppress all mentions
            // "allowed_mentions": {
            //     "parse": []
            // }
            DataObject suppressMention = DataObject.empty();
            suppressMention.put("parse", DataArray.empty());
            webhookData.put("allowed_mentions", suppressMention);
        }

        return new WebhookData(webhookData.toMap());
    }

    /**
     * Represents the data used to send a message via a Discord webhook.
     *
     * <p>Usage:</p><blockquote>{@snippet :
     *     // Creating Data
     *     WebhookData data = new WebhookDataBuilder().build();
     *     data.addFile(new File("path/to/image.png"));
     *     // Requesting Data
     *     MultipartBody body = data.prepareRequestBody();
     * }</blockquote>
     * <p>Requesting API:</p>
     * <pre>{@code
     *     RestAction restAction = new RestActionImpl<>(jda, route, body, (res, req) -> {});
     * }</pre>
     *
     * @see #addFile(File)
     * @see #prepareRequestBody()
     */
    public static class WebhookData extends DataObject implements SerializableData {
        /**
         * Maps attachment data's filename, to each respective file source.
         * (which are usable like <code>attachment://filename</code>)
         */
        private @Nullable Map<String, AttachmentSource> attachments = null;

        private WebhookData(@NotNull Map<String, Object> data) {
            super(data);
        }

        /**
         * Add a file attachment for the webhook data.
         *
         * <p>usage: put the name <code>attachment://apiName</code>
         * in any attachment field to display the file.</p>
         *
         * @param file The file source to add
         * @param apiName Filename that will be used in API request (Default to {@linkplain File#getName})
         * @throws IllegalArgumentException if the file is a directory or does not exist
         */
        public void addFile(@NotNull File file, @Nullable String apiName) {
            if(file.isDirectory()) throw new IllegalArgumentException("Cannot add folder to WebhookData.");
            if(!file.exists()) throw new IllegalArgumentException("File does not exist in the filesystem to be added.");
            if(attachments == null) attachments = new HashMap<>();

            AttachmentSource source = new AttachmentFile(file.toPath());
            attachments.put(apiName != null ? apiName : file.getName(), source);
        }

        /**
         * Add a file attachment for the webhook data.
         *
         * <p>usage: put the name <code>attachment://apiName</code>
         * in any attachment field to display the file.</p>
         *
         * @param dataURI File data URI as a base64 encoded string
         * @param apiName Filename that will be used in API request
         * @param mimeType data mimetype, is parsed using {@link MediaType#parse(String)}
         */
        public void addFile(@NotNull String dataURI, String mimeType, @NotNull String apiName) {
            if(attachments == null) attachments = new HashMap<>();
            attachments.put(apiName, new AttachmentURI(dataURI, mimeType));
        }

        /**
         * Add a file attachment for the webhook data.
         *
         * <p>usage: put the name <code>attachment://filename</code>
         * in any attachment field to display the file.</p>
         *
         * @param file The file to add
         * @throws IllegalArgumentException if the file is a directory or does not exist
         */
        public void addFile(@NotNull File file) {
            this.addFile(file, null);
        }

        /**
         * Prepares the complete request body for sending this webhook message to the Discord API.
         * <p>
         * It constructs a {@code multipart/form-data} body containing the
         * {@code payload_json} part and any file attachments provided via {@link #addFile(File)}.
         * </p>
         *
         * @return A {@link MultipartBody} representing the full webhook payload, ready to be sent.
         */
        public @NotNull MultipartBody prepareRequestBody() {

            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            tryAttachFiles(bodyBuilder);

            bodyBuilder.addFormDataPart(
                "payload_json",
                null,
                RequestBody.create(
                    MediaType.get("application/json"),
                    super.toString()
                )
            );

            return bodyBuilder.build();
        }

        /**
         * Attach files (if any) to the request body as form-data parts.
         * <p>
         * Files must be added using {@link #addFile(File)} before calling this method.
         * If any file cannot be attached, it will be silently skipped.
         *
         * @param requestBody the MultipartBody builder to add attachments to
         */
        private void tryAttachFiles(@NotNull MultipartBody.Builder requestBody) {
            List<String> attachmentIndex = putAllAttachments();

            if (attachmentIndex == null || attachments == null)  return;

            for (int i = 0; i < attachmentIndex.size(); i++) {
                String name = attachmentIndex.get(i);
                AttachmentSource source = attachments.get(name);
                InputStream data = null;

                try { data = source.get(); }
                catch (IOException | IllegalArgumentException ignored) {}

                if (data == null) continue;

                try {
                    String mimeType = source.mimeType();

                    requestBody.addFormDataPart(
                        "files[" + i + "]",
                        name,
                        new BufferedRequestBody(Okio.source(data), MediaType.parse(mimeType))
                    );
                }
                catch (RuntimeException | IOException ex) {
                    try { data.close(); }
                    catch (IOException ignored) {}
                }
            }
        }

        /**
         * Put all (if exist) file attachment to this {@link DataObject}
         * @return List of all attachments (if any) file name.
         */
        private @Nullable List<String> putAllAttachments() {
            if (attachments != null) {
                List<String> attachmentIndex = new ArrayList<>(attachments.size());
                DataArray attachmentArray = DataArray.empty();

                int i = 0;
                for (String apiName : attachments.keySet()) {
                    attachmentIndex.add(apiName);
                    DataObject attachmentObject = DataObject.empty();
                    attachmentObject.put("id", i);
                    attachmentObject.put("filename", apiName);
                    attachmentArray.add(attachmentObject);
                    i++;
                }

                put("attachments", attachmentArray);
                return attachmentIndex;
            }
            return null;
        }

        /**
         * Interface for supplying attachment source as {@linkplain InputStream}.
         *
         * @see #get()
         * @see #mimeType()
         */
        private interface AttachmentSource {
            InputStream get() throws IOException, IllegalArgumentException;
            String mimeType() throws IOException, IllegalArgumentException;
        }

        /**
         * Create an attachment source by base64 encoded data URI.
         *
         * @param dataURI The encoded data URI
         * @param mimeType data mimetype, is parsed using {@link MediaType#parse(String)}
         */
        private record AttachmentURI(String dataURI, String mimeType) implements AttachmentSource {
            @Override
            public InputStream get() throws IllegalArgumentException {
                byte[] decodedBytes = Base64.getDecoder().decode(dataURI);
                return new ByteArrayInputStream(decodedBytes);
            }
        }

        /**
         * Create an attachment source by a file path.
         *
         * @param path Path to discoverable file.
         */
        private record AttachmentFile(Path path) implements AttachmentSource {
            @Override
            public InputStream get() throws IOException, IllegalArgumentException {
                return Files.newInputStream(path);
            }

            @Override
            public String mimeType() throws IOException {
                return Files.probeContentType(path);
            }
        }
    }
}