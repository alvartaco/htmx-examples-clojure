# Base image that includes the Clojure CLI tools
FROM clojure:openjdk-19-tools-deps-bullseye

EXPOSE 3000

RUN mkdir -p /app
WORKDIR /app

# Prepare deps
COPY deps.edn /app
RUN clojure -P

# Add sources
COPY . /app

CMD clojure -M -m dumrat.htmx-learn.core
