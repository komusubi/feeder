Name:     feeder
Version:  0.3
Release:  2%{?dist}
Summary:  feed from site.

Group:    Applications/Internet
License:  Apache License 2.0
URL:      http://www.komusubi.org
Source0:  %{name}.sh
Source2:  %{name}.logrotate
Source3:  twitter4j.properties
Source4:  accessKey.properties

BuildRequires: java >= 1:1.7.0
%if 0%{?centos}
BuildRequires: apache-maven
%endif

Requires: java >= 1:1.7.0

%define homedir  %{_datadir}/%{name}
%define bindir   %{homedir}/bin
%define libdir   %{homedir}/lib
%define confdir  %{_sysconfdir}/sysconfig/feeder
%define logdir   %{_localstatedir}/log/%{name}
%define tmpdir   %{_localstatedir}/cache/%{name}
%define logrotate %{_sysconfdir}/logrotate.d
%define userbin  /usr/bin
%define artifactdir feeder-web/target
%define jar01    feeder-standalone.jar

%description
scrape html or rss and tweet.

%prep
mvn clean

#%setup -T -q -D -n .
#%{nil}

%build
#mvn -P standalone -Dmaven.test.skip=true package
mvn -P standalone package

%install
%{__rm} -rf %{buildroot}
%{__install} -dm 755 %{buildroot}%{homedir}
%{__install} -dm 755 %{buildroot}%{bindir}
%{__install} -dm 755 %{buildroot}%{libdir}
%{__install} -dm 755 %{buildroot}%{logdir}
%{__install} -dm 755 %{buildroot}%{tmpdir}
%{__install} -dm 755 %{buildroot}%{logrotate}
%{__install} -dm 755 %{buildroot}%{userbin}
%{__install} -dm 755 %{buildroot}%{confdir}

%{__install} -pm 755 %{SOURCE0} %{buildroot}%{bindir}/%{name}
%{__install} -pm 644 %{SOURCE2} %{buildroot}%{logrotate}/%{name}
%{__install} -pm 644 %{SOURCE3} %{buildroot}%{confdir}/
%{__install} -pm 644 %{SOURCE4} %{buildroot}%{confdir}/
%{__install} -pm 644 %{artifactdir}/%{jar01} %{buildroot}%{libdir}

%{__ln_s} %{logdir}         %{buildroot}%{homedir}/logs
%{__ln_s} %{tmpdir}         %{buildroot}%{homedir}/temp
%{__ln_s} %{bindir}/%{name} %{buildroot}%{userbin}/%{name}
%{__ln_s} %{confdir}        %{buildroot}%{homedir}/conf

sed -i -e "s|^\(FEEDER_HOME\)=.*$|\1=%{homedir}|" %{buildroot}%{bindir}/%{name}

%clean
%{__rm} -rf %{buildroot}

%files
%defattr(-,root,root,-)
%{homedir}
%attr(755,jun,jun) %{tmpdir}
%attr(755,jun,jun) %{logdir}
%{userbin}/%{name}
%{_sysconfdir}/logrotate.d/%{name}
%config(noreplace) %{_sysconfdir}/sysconfig/%{name}/twitter4j.properties
%config(noreplace) %{_sysconfdir}/sysconfig/%{name}/accessKey.properties

%changelog

