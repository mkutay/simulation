# Start from the universal image
FROM mcr.microsoft.com/devcontainers/universal:2

# Install openbox
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends openbox

# Set openbox to start automatically
RUN echo 'openbox-session &' >> ~/.xinitrc