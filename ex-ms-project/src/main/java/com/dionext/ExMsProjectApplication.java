package com.dionext;

import com.dionext.db.entity.JoomCategories;
import com.dionext.db.entity.JoomContent;
import com.dionext.repositories.JoomCategoriesRepository;
import com.dionext.repositories.JoomContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableScheduling
@ComponentScan
@ComponentScan(basePackages = "com.dionext.utils")
@ComponentScan(basePackages = "com.dionext.site")
public class ExMsProjectApplication  implements ApplicationRunner {

		public static void main(String[] args) {
			SpringApplication.run(ExMsProjectApplication.class, args);
		}

		@Autowired
		private Environment environment;

		@Autowired
		JoomContentRepository joomContentRepository;
		@Autowired
		JoomCategoriesRepository joomCategoriesRepository;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			String mode = getArgFirstValue(args, "mode", null);
			if (mode != null) {
				log.debug("Starting with mode " + mode);
			}
			String[] activeProfiles = environment.getActiveProfiles();
			log.info("active profiles: {}", Arrays.toString(activeProfiles));

			String path = "R:\\doc\\sites\\ideaportal\\input_out\\ru\\";
			StringBuilder content = new StringBuilder();
			content.append("""
					<html>
						<head>
						<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
						<title>Список статей</title>
						<link rel="stylesheet" type="text/css" href="images/style.css">
						</head>
						<body>
							<h1>Список статей</h1>
										""");


			List<JoomContent> list = joomContentRepository.findAll();
			for (var l : list) {
				JoomCategories cat = joomCategoriesRepository.findById(l.getCatId()).orElse(null);
				log.debug(l.getTitle());
				String title = (Strings.isNotEmpty(l.getCreatedByAlias()) ? (l.getCreatedByAlias() + " ") : "") + "\"" + l.getTitle() + "\"";
				String relpath = (cat != null?cat.getPath() + "/":"")  + l.getId() + "-" +  l.getAlias();
				String lpath = path + relpath + ".htm";
				File f = new File(lpath);
				StringBuilder str = new StringBuilder();
				str.append("""
						<html>
							<head>
							<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
							<title>
												""");
				str.append(title);
				str.append("""
						</title>
							<link rel="stylesheet" type="text/css" href="images/style.css">
							</head>
							<body>
								<h1>
												""");
				str.append(title);
				str.append("""
						</h1>
												""");
				str.append(l.getIntrotext());
				str.append(l.getFulltext());
				str.append("""
							</body>
						</html>
												""");
				Files.write(f.toPath(), str.toString().getBytes("UTF-8"));

				content.append("""
						<div>""");
				content.append("""
						<div><b><a href=\"""");
				content.append(relpath);
				content.append("""
						">""");
				content.append(title);


				content.append("""
						</a></b></div>""");

				//content.append("""
				//		<br/>""");
				content.append("""
						<div>""");
				content.append(l.getIntrotext());
				content.append("""
						</div>""");

				content.append("""
						</div>""");
			}
			content.append("""
						</body>
					</html>""");
			File f = new File(path + "content" + ".htm");
			Files.write(f.toPath(), content.toString().getBytes("UTF-8"));
		}


		private String getArgFirstValue(ApplicationArguments args, String name, String defaultValue){
			if (args.containsOption(name) && !args.getOptionValues(name).isEmpty())
				return args.getOptionValues(name).get(0);
			else return defaultValue;
		}
		private String[] getArgArrayValue(ApplicationArguments args, String name, String[] defaultValue){
			if (args.containsOption(name) && !args.getOptionValues(name).isEmpty())
				return args.getOptionValues(name).toArray(String[]::new);
			else return defaultValue;
		}
	}