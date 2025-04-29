## 12.1 사전지식 : CI/CD
- 12.1.1 CI/CD 란?
- 이 방법을 도입하면 빌드부터 배포까지의 과정을 자동화할 수 있고, 또 잘되는지 모니터링할 수 있다.
- 사실 CI는 지속적 통합, CD는 지속적 제공이라는 의미가 있다.

### 지속적 통합 CI(Continuous Integration)
- CI는 Continuous Integration 을 줄인 표현이다. 한글로 해석하면 지속적 통합이고, 풀어서 설명하면 개발자를 위해 빌드와 테스트를 자동화하는 과정이다.
- CI는 변경 사항을 자동으로 테스트해 애플리케이션에 문제가 없다는 것을 보장한다.
- 코드를 정기적으로 빌드하고, 테스트하므로 여러 명이 동시에 작업을 하는 경우 충돌을 방지하고 모니터링할 수 있다.
- ![CI.png](CI_CD%2FCI.png)
- 보통 코드 변경 사항이 코드 저장소에 업로드되면 CI를 시작하고, CI 도중 문제가 생기면 실패하므로 코드의 오류도 쉽게 파악할 수 있다.

### 지속적 제공 과 지속적 배포, CD
- CD는 CI 작업을 끝낸 다음 실행하는 작업이다. 배포 준비가 된 코드를 자동으로 서버에 배포하고 작업을 자동화 하는 것이다.
- CI 가 통과되면 개발자가 수작업으로 코드를 배포하지 않아도 자동으로 배포하니 매우 편리하다. 때문에 CD는 지속적 제공(CONTINUOUS DELIVERY)이라는 의미와 지속적 배포(CONTINUOUS DEPLOYMENT)라는 의미를 모두 가진다.

#### 지속적 제공에서의 CD 의미
- 애플리케이션에 적용한 코드의 빌드와 테스트를 성공적으로 진행했을 때 깃허브와 같은 코드 저장소에 자동으로 업로드하는 과정을 말한다.
- 최소의 노력으로 코드 배포를 쉽게 하는 것을 목표로한다.

#### 지속적 배포에서의 CD 의미
- 지속적 제공을 통해 성공적으로 병합한 코드 내역을 AWS와 같은 배포 환경으로 보내는 것을 의미한다.
- 이를 **실무에서는 릴리스**라고 한다. 지속적인 배포는 지속적 제공의 다음 단계까지 자동화한다.
- 즉, 개발자가 애플리케이션에 변겨우 사항을 커밋한 후 몇 분 이내에 애플리케이션을 자동으로 배포되어 적용된다.
- ![CD.png](CI_CD%2FCD.png)
---

### 12.1.1 git and github
- 깃(git)은 개발자가 되고 싶은 여러분이라면 한 번쯤은 들어봤을 코드를 저장하고 관리할 수 있는 시스템이다.
  - 이 시스템을 이용하면 같은 파일을 여러 명이 동시에 작업할 수 있다. 즉, 병렬개발을 할 수 있다. 깃허브(github)는 깃과 연동해 작업한 코드를 저장할 수 있는 서비스이다.
  - 여기서는 두 서비스를 이용해 앞서 배운 CI/CD를 구현해 실제로 실습 해본다.
### 깃 설치하기
#### 01 단계
- 깃 다운로드 페이지 http://git-scm.com/download/win에 접속한 뒤 [Click here to download]를 눌러 깃을 설치한다. 설치는 기본값을 그대로 두고 설치하면 된다.
  - ![git-install-win02.png](CI_CD%2Fgit-install-win02.png)
#### 02 단계
- 길 설치가 완료되면 iTerm에 실행하고 git --version 명령어를 입력한다.
- 버젼이 출력되면 제대로 나온 것이다.
- ![git version.png](CI_CD%2Fgit%20version.png)
---

### 깃허브 와 깃 연동하기
#### 01 단계
- 깃을 깃허브에 연동하려면 깃허브 회원 가입을 하고 깃 초기 설정을 해야 한다.
- 깃허브에 접속해서 회원 가입을 한 다음 git config 명령어를 사용해 깃허브에 가입한 사용자 이름 과 이메일 주소를 설정한다.
- 여기에서 설정한 정보로 커밋할 때마다 이 정보를 사용한다.
- 사용자 이름, 이메일 주소 설정
```shell
$ git config --global user.name "[깃허브 아이디]"
$ git config --global user.email "[깃허브 이메일 주소]"
```
#### 02 단계
- 또한 깃은 SSH로 접속하기 위해 인증 정보를 등록해야 하는데, PC마다 별도의 SSH 키를 등록해야한다.
- 터미널 창을 열고 SSH 키를 생성하는 명령어를 입력한다. 질문에는 모두 기본 값을 사용하게 아무것도 입력하지 않고 `enter`를 누른다.
- SSH KEY 생성
```shell
$ ssh-keygen -t rsa -C "[깃허브 이메일 주소]"
```
- ![ssh key.png](CI_CD%2Fssh%20key.png)

#### 03단계 
- 생성 완료 메시지가 뜨면 기본 경로인 /.ssh/id_rsa에 pub 파일이 생기는데 파일을 열어 값을 복사하고 이를 깃허브에 등록해야 한다.
- ssh 키가 저장되어 있는 위치로 이동한 다음 pub파일을 메모장으로 열어준다. 그 뒤에 나오는 내용을 전체 복사한다.
```shell
➜  ~ ~/.ssh
```
- ![03단계 ssh.png](CI_CD%2F03%EB%8B%A8%EA%B3%84%20ssh.png)
#### 04 단계
- 깃허브 홈페이지에 접속한 다음 프로필 사진을 누른 후 [Settings] 메뉴에 들어간다. 그런 다음 왼쪽 하단에 있는 [SSH and GPG keys]를 선택하고 [New SSH key]를 눌러 새로운 키를 등록한다.
- ![github ssh key setting.png](CI_CD%2Fgithub%20ssh%20key%20setting.png)
- ![github ssh key setting 2.png](CI_CD%2Fgithub%20ssh%20key%20setting%202.png)
#### 05 단계
- Title에 추가할 SSH 키 이름을 적고 복사 해두었던 SSH 키를 붙여넣어 준다.
- 그 뒤 [Add SSH key] 버튼을 눌러 SSH 키를 추가한다.
- 복사 명령어 `pbcopy < ~/.ssh/id_rsa.pub`
- ![github ssh key setting 4.png](CI_CD%2Fgithub%20ssh%20key%20setting%204.png)
- ![github ssh setting 3.png](CI_CD%2Fgithub%20ssh%20setting%203.png)
- 이제 연동 작업은 끝나닫. 이제 깃을 사용하기 위한 모든 준비가 끝났으니, CI/CD를 만들기 위해 깃허브 액션을 사용하며 실습을 진행해 본다.
---

## 12.2 깃허브 액션 사용하기
- 깃허브 액션(github actions)은 깃허브에서 제공하는 서비스이다. 리포지토리, 즉, 코드 원격 저장소에 특정 이벤트가 발생하면 특정 작업을 하거나ㅡ, 주기적으로 특정 작업을 반복할 수 있게 한다.
- 예를 들어 누군가 코드를 작성해 깃허브에 업데이트하면 해당 코드에 문제가 없는지 자동으로 코드를 빌드, 테스트한 이후 배포까지 할수 있다.
- 지금까지 수작업으로 이 일을 한 여러분이라면 이 서비스가 얼마나 편리할지 상상할 수 있다.

### 12.2.1 깃허브 리포지터리 생성하고 코드 푸시하기
- 깃허브 액션을 사용하려면 깃허브 리포지터리에 지금까지 작업한 코드를 업로드해야 한다.
- 깃허브에 코드를 업로드하면 행위를 푸쉬(push) 라고 부르므로 앞으로 푸시라고 이야기한다. 깃허브 리포지터리를 생성한다.

#### 01 단계
- 깃허브 홈페이지에서 [New repository] 버튼을 눌러 새 리포지터리 생성 화면으로 넘어가서 프로젝트 이름을 적은 뒤, 공개 범위를 설정한 후 리포지터리를 생성한다. 이때 실습에서 사용할 리포지터리 이름은 springboot-developer로 한다.
- 다른 이름으로 입력을 해도 된다.
- ![new repository 01 .png](CI_CD%2Fnew%20repository%2001%20.png)
- ![new repository 01 2.png](CI_CD%2Fnew%20repository%2001%202.png)

#### 02 단계
- 리포지터리가 생성되면 ssh로 접근을 할 수가 있는 리포지터리 주소도 알려준다. 주소를 복사해둔다.
- ![ssh 02 단계.png](CI_CD%2Fssh%2002%20%EB%8B%A8%EA%B3%84.png)

#### 03 단계
- 인텔리제이로 지금까지 작업한 프로젝트를 연다. 그런 다음 아래의 있는 [Terminal]을 눌러 터미널 창을 열고 `git init` 명령어를 입력한다.
- ![03단계 깃 인잇.png](CI_CD%2F03%EB%8B%A8%EA%B3%84%20%EA%B9%83%20%EC%9D%B8%EC%9E%87.png)
- `git init` 명령어는 특정 폴더를 깃 저장소로 만들 때 사용하는 명령어다. '빈 깃 저장소를 다시 초기화 했습니다' 라는 안내 문구가 나타내면 제대로 실행된 건다. 그리고 숨긴 폴더로 .git이 폴더가 생긴다. 바로 이 폴더에 코드의 변경 내역(버젼)관리를 위한 정보를 저장한다.
- 이 폴더를 실수로 지우면 여러분의 버전 관리 내역이 모두 사라지므로 주의해야한다.
  - **깃, 깃허브를 구분해서 지금까지 진행한 작업을 정리한다.** 깃허브에서는 리포지토리를 만들었고, 로컬에서는 스프링 프로젝트를 깃 저장소로 생성했다. 그림으로 보면 다음과 같다.
  - ![git repository.png](CI_CD%2Fgit%20re![05단계 git add.png](..%2F..%2F..%2FDesktop%2F05%EB%8B%A8%EA%B3%84%20git%20add.png)pository.png)

#### 04 단계
- 이번에는 깃허브의 리포지토리와 로컬의 깃 저장소를 연결하기 위해 remote 명령어를 사용한다.
- 쉽게 말해 로컬의 깃 저장소 이력과 파일을 모두 깃 허브의에 업로드하기 위해 이 둘을 연결한다고 생각하면 된다.
- 깃허브의 리포지터리 주소는 아까 복사했으니 이 값을 다음 명령어에 잘 넣어 입력하면 된다.
- remote로 깃, 깃허브 연결하기
```shell
$ git remote add origin git@github.com:${사용자계정}/springboot-developer.git
```
- 명령어 입력을 완료하고 나면 origin이라는 다축 이름에 git@github.com:${사용자계정명}/springboot-developer.git이라는 리포지토리를 추가한다.
- ![04단계 github contact.png](CI_CD%2F04%EB%8B%A8%EA%B3%84%20github%20contact.png)

#### 05 단계
- 이제 로컬 저장소의 이력, 파일을 리포지토리에 푸 시하기 위한 add, commit 작업을 해본다.
- add . 명령어는 현재 프로젝트 폴더의 모든 파일을 대상으로 변경 사항 등을 추적하고 그 파일들을 스테이지라는 곳에 올린다. 스테이지는 쉽게 말해서 리포지토리에 올리기 전에 파일들의 변경 사항을 미리 모아놓는 곳이다.
- commit . 로컬 저장소에 올리기 위한 것으로 , 즉 커밋을 해야만 로컬 저장소에 변경 이력, 변경한 파일들이 업데이트 된다.
- ![05단계 git add.png](CI_CD%2F05%EB%8B%A8%EA%B3%84%20git%20add.png)
```shell
# remote 변경하기
➜  springboot-developer git:(main) git remote -v
origin  https://github.com/azsx92/springboot-blog.git (fetch)
origin  https://github.com/azsx92/springboot-blog.git (push)
➜  springboot-developer git:(main) git remote set-url origin git@github.com:azsx92/springboot-developer.git

```

#### 06 단계
- 브랜치명을 main으로 바꾼 후 원격 저장소에 저장하기 위해 push 명령어를 입력해 푸시를 마무리 한다. 이제 깃허브 리포지토리에 코드가 업데이트 되었다.
```shell
$ git branch -M main
$ git push origin main

➜  springboot-developer git:(main) git push
오브젝트 나열하는 중: 833, 완료.
오브젝트 개수 세는 중: 100% (833/833), 완료.
Delta compression using up to 12 threads
오브젝트 압축하는 중: 100% (634/634), 완료.
오브젝트 쓰는 중: 100% (833/833), 9.44 MiB | 3.39 MiB/s, 완료.
Total 833 (delta 262), reused 0 (delta 0), pack-reused 0 (from 0)
remote: Resolving deltas: 100% (262/262), done.
To github.com:azsx92/springboot-developer.git
 
```

#### 07 단계 
- 깃허브에 접속해 리포지토리를 확인하면 커밋할 때 적었던 메시지와 함께 코드들이 업로드된 것을 확인할 수 있다.
- ![07단계 git push.png](CI_CD%2F07%EB%8B%A8%EA%B3%84%20git%20push.png)

---

### 12.2.2 깃허브 액션 스크립트 작성하기 , CI
- 이제 깃허브에 리포지토리가 준비되었으니 깃허브 액션 스크립트를 작성해 CI를 구현한다.

#### 01 단계
- 프로젝트 최상단에 .github 디렉터리를 만든다. 그 안에 workflows 디렉터리를 다시 만들고 ci.yml 파일을 생성해 다음 스크립트를 작성한다.
- 여기서 주의 할 점은 디렉토리 명을 workflow로 하지 않기로 한다!
- ![01 단계  12.2.2 .png](CI_CD%2F01%20%EB%8B%A8%EA%B3%84%20%2012.2.2%20.png)
```yaml
# 1. 워크플로의 이름 지정
name: CI

# 2. 워크플로가 시작될 조건 지정
on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: macos-latest # Ubuntu 환경은 ubuntu-latest, Windows는 windows-latest로 지정
    # 4 실행 스텝 지정
    steps:
      - uses: actions/sheckout@v3
        
      - uses: actions/setup-java@v3
        with:
          distribution: 'zulu'
          java-version: '17'
          
      - name: Grant execute permission for gradlew
      - run: chmod +x gradlew
      
      - name: Build with Gradle
        run: ./gradlew clean build
```
1. 워크플로 이름을 지정
2. 워크플로를 시작할 트리거 조건을 지정, main 브랜치에 푸시를 할때 마다 워크플로를 시작하도록 작성했다.
3. 리눅스나 원도우와 같은 실행 환경을 지정한다. 필자는 macOS로 지정
4. 실행 스텝을 그룹화한다. 각 항목은 별도의 작업uses 또는 명령어run로 이루어 졌다.
5. 실행 스텝 그룹화 정리
   6. users : users 키워드는 지정한 리포지토리를 확인하고 코드에 대한 작업을 실행 할 수 있다. action/check-out에는 checkout이라는 작업의 v3 버젼을 실행한다.
   7. naem : 스텝의 이름을 지정한다.
   8. run : run 키워드는 실행할 명령어를 입력한다. ./gradlew clean build에는 그레들을 사용해 프로젝트를 빌드 이전 상태로 돌리고 다시 빌드하는 명령어를 실행한다.

#### 02 단계
- 추가된 파일을 원격 저장소에 올리기 위해 커밋, 푸시를 진행하고 깃허브 리포지터리의 [Action] 메뉴에 들어가 CI가 실행되는 것을 확인한다.
- 주의 할 점은 : yaml 문법이 틀렸는지 확인한다.
```shell
➜  springboot-developer git:(main) ✗ git add .
➜  springboot-developer git:(main) ✗ git commit -m 'CI 추가'
➜  springboot-developer git:(main) git push origin main
```
- ![02단계 12.2.2.png](CI_CD%2F02%EB%8B%A8%EA%B3%84%2012.2.2.png)
- 워크플로가 성공적으로 동작하면 초록색 체크 모양으로 표시된다. 여기까지 호가인한 뒤에 CD 스크립트를 추가한다.

---

### 12.2.3 깃허브 액션 스크립트 작성하기 CD

#### 01 단계
- 현재 프로젝트에서는 빌드를 진행하면 총 두개의 jar 파일이 생긴다. 하나는 일반 jar 파일이고 다른 하나는 plain이라는 접미사가 붙은 jar 파일이다.
- ![12.2.3 01 단계.png](CI_CD%2F12_2_3%2F12.2.3%2001%20%EB%8B%A8%EA%B3%84.png)
  - 이 jar 플레인 아카이브(plain archive)라고 하며 애플리케이션 실행에 필용한 의존성을 포함하지 않고 소스 코드의 클래스 파일과 리소스 파일만 포함한다. 따라서 플레인 아카이브만으로는 서비스를 실행할 수 없으므로 빌드 시에 일반 jar 파일만 생성하도록 그레이들 파일을 변경하겠다.
```groovy
/*build.gradle*/
jar {
    enabled = false
}
```
#### 02 단계
- 깃허브 액션 스크립트에서 만든 ci.yaml 파일 이름을 cicd.yaml로 변경하고 다음 코드를 추가한다.
```yaml
# 1. 워크플로의 이름 지정
name: CI

# 2. 워크플로가 시작될 조건 지정
on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: macos-latest # Ubuntu 환경은 ubuntu-latest, Windows는 windows-latest로 지정

    steps:
      - uses: actions/checkout@v3

      - uses: actions/setup-java@v3
        with:
          distribution: 'zulu'
          java-version: '17'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build with Gradle
        run: ./gradlew clean build

      # 2. 현재 시간 가져오기
      - name: Get current time
        uses: josStorer/get-current-time@v2.0.2
        id: current-time
        with:
          format: YYYY-MM-DDTHH-mm-ss
          utcOffset: "+09:00"

      # 3. 배포용 패키지 경로 저장
      - name: Set artifact
        run: echo "artifact=$(ls ./build/libs)" >> $GITHUB_ENV

      # 4. 빈스토크 배포
      - name: Beanstalk Deploy
        uses: einaregilsson/beanstalk-deploy@v20
        with:
          aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          application_name: springboot-developer
          environment_name: springboot-developer_env
          version_label: github-action-${{ steps.current-time.outputs.formattedTime }}
          region: ap-northeast-2
          deployment_package: ./build/libs/${{ env.artifact }}

```
1. 깃허브 액션 이름을 CI에서 CI/CD로 변경 한다.
2. josStorer/get-current-time 플러그인을 통해 현재 시간을 가져 온다. 가져온 시간은 배포 버젼을 지정할 때 사용한다.
3. 빌드 이후에 생성된 jar 파일로 찾아 'artifact' 라는 환겨우 변수에 값을 넣어 준다. $GITHUBENV를 사용해 깃허브 워크프로 전체적으로 사용할 수 있는 환경 변수를 설정 할 수 있다. 
4. einaregilsson/beanstalk-deploy 플러그인을 사용해 빈스토크 배포를 진행한다. 여기서 지정한 secret.AWS_ACCESS_KEY_ID 와 secrets.AWS_SECRET_ACCESS_KEY 는 깃허브 액션에서 가져오는 비밀 값이다. 이 값은 AWS에서 만든 뒤 깃허브에 설정해야 한다. 또한 애플리케이션 이름 과 환경 이름을 일래스틱 빈스토크에서 확인 할 수 있다.
#### 03 단계
- IAM은 AWS 리소스를 사용하도록 권한을 부여하는 서비스이다.
  - AWS에 접속한 뒤 IAM 서비스를 검색해 접속한 다음 [사용자]를 클릭한다. 그런 다음 [사용자 추가] 버튼을 눌러 사용자 추가한다.
  - 사용자 이름은 github-action으로 지정한다.
- ![12.2.3 04 .png](CI_CD%2F12_2_3%2F12.2.3%2004%20.png)
#### 04 단계
- [다음] 을 눌러 나온 권한 설정에서는 [직접 정책 연결]을 선택한 뒤 AdministratorAccess-AWSElasticBeanstalk를 검색해 선택한다.
- 이 권한은 빈스토크를 사용하기 위해 필요한 모든 관리 권한을 사용자에게 제공하는 권한이다.
- AdministratorAccess 권한은 너무 광범위하기 때문에 정말 필요한 권한만 주는 것이 좋지만 진행의 편의를 위해 해당 권한을 사용한다.
- ![12.2.3 04 2.png](CI_CD%2F12_2_3%2F12.2.3%2004%202.png)
- ![12.2.3 04 3.png](CI_CD%2F12_2_3%2F12.2.3%2004%203.png)
#### 05 단계
- 사용자 생성을 마치고 github-action 사용자를 눌러 액세스 키를 만든다. 조금만 스크롤바를 내리면 액세스 키 항목의 [액세스 키 만들기] 버튼을 찾을 수 있다.
- [서드 파티 서비스]를 선택하고 [다음]을 누르고 '설명 태그 값'을 github-action으로 해 액세스 키를 만든다.
- ![12.2.3 05.png](CI_CD%2F12_2_3%2F12.2.3%2005.png)
- ![12.2.3 05 3.png](CI_CD%2F12_2_3%2F12.2.3%2005%203.png)
- ![12.2.3 05 4.png](CI_CD%2F12_2_3%2F12.2.3%2005%204.png)
#### 06 단계
- 그러면 액세스 키가 만들어 진다. **이 화면을 넘기지말고 액세스 키는 이 화면에서 딱 한 번 확인할 수가 있다.** 
- 값을 미리 복사하거나 [.csv 파일 다운로드]를 눌러 보관해둔다.
- ![12.2.3 06 1.png](CI_CD%2F12_2_3%2F12.2.3%2006%201.png)
#### 07 단계
- 복사한 값을 등록하기 위해 깃허브 리포지토리에 접속한 뒤 [Setting -> Secret and variables -> Actions] 순서로 메뉴에 들어간 후 [New repository secrets] 버튼을 눌러 새로운 비밀 키를 각각 등록한다.
- ![12.2.3 07 1.png](CI_CD%2F12_2_3%2F12.2.3%2007%201.png)
- ![12.2.3 07 2.png](CI_CD%2F12_2_3%2F12.2.3%2007%202.png)
#### 08 단계
- 깃허브에 커밋 , 푸시를 하기 전에 민감한 값을 삭제한다. application.yaml 파일을 열어 비밀값으로 정의한 client_id 와 client_secret, 그리고 jwt 항목을 삭제한다.
#### 09 단계
- cd가 정상적으로 작동하는 것을 확인하기 위해 커밋과 푸시를 차레대로 수행하고 확인한다.
- 깃허브 액션이 성공하는 것을 확인할 수 가 있다. 실제로 배포가 되었는지 확인하기 위해 빈스토크의 최근 배포 날짜와 시간을 확인해 본다. 
- 앞으로 작업을 한 뒤 리포지터리에 업로드하면 깃허브 액션이 빌드를 자동으로 실행하고, 빌드에 성공하면 새 버젼을 빈스토크에 배포한 것이다.
```shell
$ git add .
$ git commit -m "ci.yml > cicd.yml" 
$ git push origin main" 
```
- ![12.2.3 09 1.png](CI_CD%2F12_2_3%2F12.2.3%2009%201.png)
- ![12.2.3 09 2.png](CI_CD%2F12_2_3%2F12.2.3%2009%202.png)
