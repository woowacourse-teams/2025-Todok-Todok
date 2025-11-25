resource "aws_instance" "todoktodok-dev" {
  ami           = "ami-00d3651c54d49a6fa"
  instance_type = "t4g.micro"

  root_block_device {
      volume_type = "gp3"
      volume_size = 16
  }
  key_name = "key-todoktodok"

  subnet_id = aws_subnet.public-a.id
  vpc_security_group_ids = [aws_security_group.app-sg.id]

  tags = {
    Name = "todoktodok-dev"
    Project = "todoktodok"
    Type = "dev"
  }
}

resource "aws_instance" "todoktodok-monitoring" {
  ami           = "ami-0d9198cb565eb3763"
  instance_type = "t4g.micro"

  root_block_device {
      volume_type = "gp3"
      volume_size = 16
  }

  key_name = "key-todoktodok"

  subnet_id = aws_subnet.public-a.id
  vpc_security_group_ids = [aws_security_group.app-sg.id]

  tags = {
    Name = "todoktodok-monitoring"
    Project = "todoktodok"
    Type = "prod"
  }
}

resource "aws_instance" "todoktodok-nginx" {
  ami           = "ami-0f73204b8919b316f"
  instance_type = "t4g.micro"

  root_block_device {
      volume_type = "gp3"
      volume_size = 8
  }

  key_name = "key-todoktodok"

  subnet_id = aws_subnet.public-a.id
  vpc_security_group_ids = [aws_security_group.app-sg.id]

  tags = {
    Name = "todoktodok-nginx"
    Project = "todoktodok"
    Type = "prod"
  }
}

resource "aws_instance" "todoktodok-prod" {
  ami           = "ami-00bbd6d134e3564ff"
  instance_type = "t4g.micro"

  root_block_device {
      volume_type = "gp3"
      volume_size = 16
  }

  key_name = "key-todoktodok"

  subnet_id = aws_subnet.public-a.id
  vpc_security_group_ids = [aws_security_group.app-sg.id]

  tags = {
    Name = "todoktodok-prod"
    Project = "todoktodok"
    Type = "prod"
  }
}

resource "aws_instance" "todoktodok-db" {
  ami           = "ami-0e7c7b20c7ef41487"
  instance_type = "t4g.micro"

  root_block_device {
      volume_type = "gp3"
      volume_size = 16
  }

  key_name = "key-todoktodok"

  subnet_id = aws_subnet.private-a.id
  vpc_security_group_ids = [aws_security_group.db-sg.id]

  tags = {
    Name = "todoktodok-db"
    Project = "todoktodok"
    Type = "prod"
  }
}

# 네트워크

# 1. VPC 생성
resource "aws_vpc" "todoktodok-vpc" {
  cidr_block = "10.0.0.0/16" # VPC의 IP 주소 범위 (예: 65,536개 주소)

  tags = {
    Name = "todoktodok"
  }
}

# 2-A. 인터넷 게이트웨이 생성 및 VPC 연결
resource "aws_internet_gateway" "todoktodok-igw" {
  vpc_id = aws_vpc.todoktodok-vpc.id # 위에서 생성한 VPC에 연결

  tags = {
    Name = "todoktodok"
  }
}

# 2-B. 퍼블릭 라우팅 테이블 생성
resource "aws_route_table" "public-rt" {
  vpc_id = aws_vpc.todoktodok-vpc.id

  # 외부(0.0.0.0/0)로 나가는 모든 트래픽을 인터넷 게이트웨이로 보냄
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.todoktodok-igw.id
  }

  tags = {
    Name = "todoktodok"
  }
}

# 2-C. 퍼블릭 서브넷 생성 (VPC CIDR 내의 범위 지정)
resource "aws_subnet" "public-a" {
  vpc_id                  = aws_vpc.todoktodok-vpc.id
  cidr_block              = "10.0.1.0/24" # 256개 주소, vpc 주소 범위 10.0.0.0 ~ 10.0.255.255 중 아무거나 상관없음
  availability_zone       = "ap-northeast-2a"
  # 👇 중요: 퍼블릭 서브넷의 인스턴스에 퍼블릭 IP를 자동 할당
  map_public_ip_on_launch = true

  tags = {
    Name = "todoktodok"
  }
}

# 2-D. 퍼블릭 서브넷과 라우팅 테이블 연결
resource "aws_route_table_association" "public-a" {
  subnet_id      = aws_subnet.public-a.id
  route_table_id = aws_route_table.public-rt.id
}

# 3-A. 프라이빗 서브넷 생성
resource "aws_subnet" "private-a" {
  vpc_id                  = aws_vpc.todoktodok-vpc.id
  cidr_block              = "10.0.10.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = false # 프라이빗 IP만 할당 (퍼블릭 IP 자동 할당 비활성화)

  tags = {
    Name = "todoktodok"
  }
}

# 3-B. 프라이빗 라우팅 테이블 정의
# (인터넷 게이트웨이로의 경로를 지정하지 않거나 NAT 게이트웨이로 경로를 지정)
resource "aws_route_table" "private-rt" {
  vpc_id = aws_vpc.todoktodok-vpc.id

  tags = {
    Name = "private-route-table"
  }
}

# 3-C. 프라이빗 서브넷과 라우팅 테이블 연결
resource "aws_route_table_association" "private_a" {
  subnet_id      = aws_subnet.private-a.id
  route_table_id = aws_route_table.private-rt.id
}

# 기본 보안그룹
resource "aws_security_group" "app-sg" {
  name        = "app-sg"
  description = "Allow SSH and web traffic"
  vpc_id = aws_vpc.todoktodok-vpc.id

  # SSH
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTP
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTPS
  ingress {
      from_port   = 443
      to_port     = 443
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  # Spring Boot (8080)
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # 아웃바운드 전체 허용
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "db-sg" {
  name   = "db-sg"
  vpc_id = aws_vpc.todoktodok-vpc.id

  ingress {
      from_port   = 22
      to_port     = 22
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.app-sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
