<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# 나 git action으로 어떻게  일레스틱빈스톡에다가  배포를 하는게 궁금해

# 1. 워크플로의 이름 지정

name: CI

# 2. 워크플로가 시작될 조건 지정

on:
push:
branches: [main]

jobs:
build:
runs-on: macos-latest \# Ubuntu 환경은 ubuntu-latest, Windows는 windows-latest로 지정

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
          environment_name: Springboot-developer-env-1
          version_label: github-action-${{ steps.current-time.outputs.formattedTime }}
          region: ap-northeast-2
          deployment_package: ./build/libs/${{ env.artifact }}
    GitHub Actions를 통해 Elastic Beanstalk에 배포하는 전체 프로세스와 주의사항을 설명드리겠습니다. 검색 결과와 현재 워크플로우 파일을 기반으로 핵심 내용을 정리했습니다.

---

### **1. AWS IAM 설정 (필수 사전 작업)**

- **IAM 사용자 생성**: AWS 콘솔에서 GitHub Actions 전용 사용자 생성[^1][^7]
    - 권한 정책: `AdministratorAccess-AWSElasticBeanstalk` 필수 부여[^1][^7]
    - 액세스 키 생성 후 **GitHub Secrets**에 등록:
      `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`[^5][^9]

---

### **2. 워크플로우 주요 수정 포인트**

```yaml
- name: Beanstalk Deploy
  uses: einaregilsson/beanstalk-deploy@v20
  with:
    aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}  # secrets 이름 확인 필수
    aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
    application_name: springboot-developer  # EB 애플리케이션 이름과 정확히 일치
    environment_name: Springboot-developer-env-1  # EB 환경 이름과 정확히 일치
    deployment_package: ./build/libs/*.jar  # 패키지 경로 수정 권장
```


#### **문제 발생 가능 항목**

1. **`deployment_package` 경로 오류**
    - 현재 방식: `${{ env.artifact }}` → **공백 포함 파일명 있을 시 실패**
    - 해결 방안:

```yaml
run: echo "artifact=$(ls ./build/libs/*.jar | head -n 1)" &gt;&gt; $GITHUB_ENV
```

→ **첫 번째 jar 파일만 선택**하도록 수정[^1]
2. **Beanstalk 환경 이름 불일치**
    - AWS 콘솔에서 **실제 환경 이름 확인 필수** (대소문자 주의)[^9]

---

### **3. 추가 권장 사항**

- **Procfile 추가**: 프로젝트 루트에 실행 명령어 정의[^2]

```procfile
web: java -jar build/libs/your-app.jar
```

- **`version_label` 최적화**:

```yaml
version_label: ${{ github.sha }}-${{ steps.current-time.outputs.formattedTime }}
```

→ Git 커밋 해시 추가로 버전 추적 용이[^9]

---

### **4. 오류 발생 시 체크리스트**

1. **IAM 권한 재확인**: `AdministratorAccess-AWSElasticBeanstalk` 정책 필수[^1][^7]
2. **Secrets 이름 일치 여부**:

```yaml
aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}  # secrets.AWS_EB_ACCESS_KEY_ID 등으로 변경 시 양쪽 일치
```

3. **패키지 경로 실제 존재 여부**:

```bash
ls ./build/libs/  # 빌드 결과물 확인
```


---

### **5. 전체 흐름 요약**

1. **코드 푸시** → GitHub Actions 트리거
2. **빌드 단계**: Gradle 실행 → `build/libs/*.jar` 생성
3. **배포 단계**:
    - AWS 자격 증명 확인
    - 지정된 jar 파일을 Elastic Beanstalk에 배포
    - 새 버전으로 환경 업데이트

검색 결과[^5][^7][^9]를 종합한 실제 배포 성공 사례와 현재 워크플로우를 비교해보시면 더욱 명확히 이해되실 겁니다.

<div style="text-align: center">⁂</div>

[^1]: https://velog.io/@wldns2577/Github-Action-Elastic-Beanstalk으로-CICD

[^2]: https://wonit.tistory.com/597

[^3]: https://velog.io/@black2code/Github-Action-과-Elastic-Beanstalk-배포-자동화

[^4]: https://jojoldu.tistory.com/549

[^5]: https://twosky.tistory.com/56

[^6]: https://insight-bgh.tistory.com/525

[^7]: https://dingdingmin-back-end-developer.tistory.com/entry/Elastic-Beanstalk-정복하기-7-Single-Instance-CICD-Github-Action

[^8]: https://dodo-studying.tistory.com/566

[^9]: https://hello-judy-world.tistory.com/165

