<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="#readme-top">
    <img src="/app/src/main/res/drawable/vsiyp_logo.png" alt="Logo" width="80px" height="80px">
  </a>

  <h3 align="center">VSIYP</h3>

  <p align="center">
    Video Sharing in Your Pocket~ Have the power to quickly process your videos and share your memories to your love ones! 
    <br />
    <br />
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
	      <li><a href="#project-structure">Project Structure</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

VSIYP is a video processing application based on Android, with the intention of creating a good UI/UX design, so that users will be able to quickly do special image or video processing features. Utlizing the Huawei Video Editor Kit SDK, the app will allow users to preview a live camera feed of the smartphone’s built-in camera, take a snap-shot photo or short video clip, perform image processing, photo or video editing features with the 27 video processing tools provided, before saving or sharing the final photo/video through social networks via WhatsApp, WeChat, Telegram, Instagram, Facebook, etc. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Project Structure

```
|-- home
	|-- MainActivity: video creation page, which can be switched to the editing screen (**ClipFragment**) and template home screen (**TemplateHomeFragment**).
	|-- MediaPickActivity: material selection screen. When there is a need to select materials from the album, this screen will be launched.
	|-- VideoClipsActivity: video editing screen, which can be entered via material import for creation or a draft. The upper part of the screen is the preview area, the middle part contains playback operations and the timeline, and the lower part is the two-level menu area.
	|-- MaterialEditFragment: preview area, where a material can be edited. It is the entry to the zooming in or out using two fingers, dragging, rotating, and other operations on the material selected on the preview area.
	|-- MenuClickManager: manager for menu clicks. The menu click event on the video editing screen is processed in the **handlerClickEvent** method. Each click event launches a corresponding fragment. All fragments inherit the **BaseFragment** abstract class, implementing the following methods: **initView**, **initObject**, **initData**, and **initEvent**. **initView** initializes the layout and component. The listener for the component is created in the **initEvent** method, to respond to the click event in each fragment and conduct relevant service logic processing.

	|-- Editing-related functions:
		|-- AssetCropFragment: cropping
		|-- AssetSplitFragment: splitting
		|-- GeneralSpeedFragment: video playback speed adjustment
		|-- AnimationPanelFragment: animation
		|-- StickerPanelFragment: sticker
		|-- EditPanelFragment: text
		|-- EditTextStyleFragment: text style
		|-- EditTextAnimateFragment: text animation
		|-- EditTextBubblesFragment: text bubble
		|-- EditTextFlowerFragment: artistic font
		|-- FilterPanelFragment: filter
		|-- EffectPanelFragment: special effect
		|-- FilterAdjustPanelView: adjustment
		|-- TransparencyPanelFragment: transparency
		|-- VideoProportionFragment: canvas ratio
		|-- CanvasBackgroundFragment: canvas background
		|-- AudioPickActivity and MusicLocalFragment: adding music
		|-- SoundEffectFragment: adding a sound effect
		|-- AudioSpeedFragment: audio playback speed adjustment
		|-- VolumePanelFragment: volume
		|-- CropNewActivity: cropping
		
	|-- Export-related functions:
		|--VideoExportActivity: main screen for export
		|--ExportFragment: parameter configuration before export, export progress, and export failure
		|--ExportSuccessFragment: export success
```

### Built With

This project is built by using these frameworks/libraries:

- Android Studio 
- Java
- Kotlin
- XML 
- Huawei Video Editor Kit SDK 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

```sh
1. Create a Huawei Developer Account
2. Create a new application
3. Download the API key file, agconnect-services.json.
```

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/SQUEK011/cs2024-sg-money-lender.git
   ```
2. Open the repo using Android Studio
3. Insert your agconnect-services.json file at the root of your file 
4. Change the API key to your file's API key under MainActivity.kt
5. Run the application 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

In Production

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>-->

<!-- CONTACT -->

## Contact

For any questions or bugs encountered in the codes, please send an email to shenanquek97@gmail.com or send a pm to me.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!--acknowledgments-->

## Acknowledgments

This project was created by Shen An for Final Year Project in Nanyang Technological University (NTU).

Logo and Image Assets was created using [![Canva][canva.com]][canva-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[html-url]: https://html.spec.whatwg.org/multipage/
[html.com]: https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white
[css-url]: https://www.w3.org/Style/CSS/Overview.en.html
[w3.org]: https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white
[bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[bootstrap-url]: https://getbootstrap.com
[canva.com]: https://img.shields.io/badge/Canva-%2300C4CC.svg?&style=for-the-badge&logo=Canva&logoColor=white
[canva-url]: https://canva.com
[vscode-img]: https://img.shields.io/badge/Made%20for-VSCode-1f425f.svg
