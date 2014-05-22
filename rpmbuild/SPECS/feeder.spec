Name:     feeder
Version:  0.2
Release:  1%{?dist}
Summary:  feed from site.

Group:    Applications/Internet
License:  Apache License 2.0
URL:      http://www.komusubi.org
Source0:  %{name}.sh
#Source2:  %{name}.logrotate
#Source3:  %{name}.service

BuildRequires: java >= 1:1.7.0
%if 0%{?centos}
  BuildRequires: apache-maven
%endif

Requires: java >= 1:1.7.0

#%define debug_package %{nil}
#%define hostarch %{_host_os}-%{__isa_name}-%{__isa_bits}
%define homedir  %{_datadir}/%{name}
%define bindir   %{homedir}/bin
%define logdir   %{_localstatedir}/log/%{name}
%define tmpdir   %{_localstatedir}/cache/%{name}
#%define confdir  %{_sysconfdir}/%{name}
#%define logrotate %{buildroot}%{_sysconfdir}/logrotate.d
#%if 0%{?fedora}
#  %define unit     %{buildroot}%{_unitdir}/%{name}.service
#%endif
#%if 0%{?centos}
#  %define unit     %{buildroot}%{_initddir}/%{name}
#%endif

%description
scrape html or rss and tweet.

%prep
mvn clean

#%setup -T -q -D -n .
#%{nil}

%build
mvn -P standalone -Dmaven.test.skip=true package

%install
%{__rm} -rf %{buildroot}
%{__install} -dm 755 %{buildroot}%{homedir}
%{__install} -dm 755 %{buildroot}%{bindir}

%{__install} -dm 755 %{buildroot}%{logdir}
%{__install} -dm 755 %{buildroot}%{tmpdir}
#%if 0%{?fedora}
#  %{__install} -dm 755 %{buildroot}%{_unitdir}
#%endif
#%if 0%{?centos}
#  %{__install} -dm 755 %{buildroot}%{_initddir}
#%endif

#%{__cp} -r conf             %{buildroot}%{homedir}
#%{__cp} -r bin/%{hostarch}  %{buildroot}%{homedir}/bin/
#%{__cp} -r data             %{buildroot}%{homedir}
#%{__cp} -r lib              %{buildroot}%{homedir}
#%{__cp} -r extensions       %{buildroot}%{homedir}
#%{__cp} -r web              %{buildroot}%{homedir}

%{__ln_s} %{logdir}       %{buildroot}%{homedir}/logs
%{__ln_s} %{tmpdir}       %{buildroot}%{homedir}/temp
#ln -sf %{homedir}/conf %{buildroot}%{confdir}

#sed -e "s|@@SCRIPT_PATH@@|%{homedir}/bin/%{hostarch}/sonar.sh|g" %{SOURCE2} > %{name}.service
#sed -e "s|@@LOG_FILE_PATH@@|%{logdir}/sonar.log|g" %{SOURCE0}
#install -pm 644 %{SOURCE0} %{buildroot}%{_sysconfdir}/logrotate.d/%{name}
#sed -e "s|^\(sonar.jdbc.url\)=.*$|\1=http:\/\/localhost|" 

#%{__install} -pm 644 %{SOURCE2} %{unit}
#%{__sed} -i -e "s|@@SCRIPT_PATH@@|%{homedir}/bin/%{hostarch}/sonar.sh|g" %{unit}

#%{__install} -pm 644 %{SOURCE0} %{logrotate}/%{name}

# install configuration scripts for mariadb 
#%{__install} -pm 755 %{SOURCE10} %{buildroot}%{homedir}/bin/%{name}-mariadb

# sed max_allowed_packet = 4194304

%clean
%{__rm} -rf %{buildroot}

%post
#%systemd_post %{name}

%preun
#%systemd_preun %{name}

%postun
#%systemd_postun_with_restart %{name}

%files
%defattr(-,root,root,-)
%{homedir}
%{tmpdir}
%{logdir}
#%{_unitdir}/%{name}.service
%{_sysconfdir}/logrotate.d/%{name}

%changelog

