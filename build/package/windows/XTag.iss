;This file will be executed next to the application bundle image
;I.e. current directory will contain folder XTag with application files
[Setup]
AppId={{fxApplication}}
AppName=XTag
AppVersion=3.0.0
AppVerName=XTag 3.0.0
AppPublisher=fer.hr.telegra
AppComments=TaggingApp
AppCopyright=Copyright (C) 2018
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf}\XTag
DisableStartupPrompt=No
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=No
DisableFinishedPage=No
DisableWelcomePage=No
DefaultGroupName=fer.hr.telegra
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=XTag-3.0.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=XTag\XTag.ico
UninstallDisplayIcon={app}\XTag.ico
UninstallDisplayName=XTag
WizardImageStretch=No
WizardSmallImageFile=XTag-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Dirs]
Name: "{app}"; Permissions: users-full

[Files]
Source: "XTag\XTag.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "XTag\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\XTag"; Filename: "{app}\XTag.exe"; IconFilename: "{app}\XTag.ico"; Check: returnTrue()
Name: "{commondesktop}\XTag"; Filename: "{app}\XTag.exe";  IconFilename: "{app}\XTag.ico"; Check: returnFalse()


[Run]
Filename: "{app}\XTag.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\XTag.exe"; Description: "{cm:LaunchProgram,XTag}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\XTag.exe"; Parameters: "-install -svcName ""XTag"" -svcDesc ""XTag"" -mainExe ""XTag.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\XTag.exe "; Parameters: "-uninstall -svcName XTag -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
