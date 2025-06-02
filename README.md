# discordsrv-bridge
[![Maven Central](https://img.shields.io/maven-central/v/asia.buildtheearth.asean.discord/discordsrv-bridge)](https://central.sonatype.com/artifact/asia.buildtheearth.asean.discord/discordsrv-bridge)
[![javadoc](https://javadoc.io/badge2/asia.buildtheearth.asean.discord/discordsrv-bridge/javadoc.svg)](https://javadoc.io/doc/asia.buildtheearth.asean.discord/discordsrv-bridge)
[![GitHub license](https://img.shields.io/github/license/ASEAN-Build-The-Earth/discordsrv-bridge)](https://github.com/ASEAN-Build-The-Earth/discordsrv-bridge/blob/main/LICENSE)

Bridge utility library for connecting to DiscordSRV JDA

## [Maven Central Package](https://central.sonatype.com/artifact/asia.buildtheearth.asean.discord/discordsrv-bridge)

Include the central repository
```xml
<repository>
    <id>sonatype</id>
    <url>https://oss.sonatype.org/content/groups/public/</url>
</repository>
```
Include this library
```xml
<dependency>
    <groupId>asia.buildtheearth.asean.discord</groupId>
    <artifactId>discordsrv-bridge</artifactId>
    <version>1.1.1</version>
</dependency> 
```

--- 

# Packages Hierarchy
<table><tbody><tr><td><sub>

```py
 /src/main/java/asia.buildtheearth.asean.discord
 ├─ 📦commands
 │  └─ 📦events
 │  └─ 📦interactions
 │
 ├─ 📦components
 │  ├─ 📦api
 │  └─ 📦buttons
 │
 └─ 📦providers
```
</sub>
</td></tr></tbody></table>

### 📦commands
Use this package to manage discord slash command interactions.
The class `SlashCommand` can be used as the base class of all slash command implementations.

<b>📦commands.events</b>
> Manage slash command event (Current only provides the handle for slash command trigger event)

<b>📦commands.interactions</b>
> Manage slash command interaction as payload class and its event.

### 📦components
This package handle discord's message components.

<b>📦components.api</b>
> New Discord's message components API ([ComponentV2](https://discord.com/developers/docs/components/reference))

<b>📦components.buttons</b>
> Discord's Button components handler

<b>📁IDPattern</b>
> Plugin Component's ID pattern that is
  used to parse component's `custom_id` payload.

<b>📁PluginComponent</b>
> A class to parse plugin registered components.

<b>📁WebhookDataBuilder</b>
> A builder for creating `WebhookData` objects, which represent payloads for sending messages via Discord webhooks.
  Supports thread name, username, avatar URL, message content, embeds, and interaction components.


### 📦providers

<b>📁ComponentProvider</b>
> Plugin owned component provider class.
  Use this interface to create each component ID.

<b>📁DiscordCommandProvider</b>
> Provider/Manager for discord slash command interactions.
  Managed externally by DiscordSRV API {@link SlashCommandProvider}.
