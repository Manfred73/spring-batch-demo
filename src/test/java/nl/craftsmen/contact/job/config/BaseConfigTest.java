package nl.craftsmen.contact.job.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;

@ExtendWith(MockitoExtension.class)
class BaseConfigTest {

	@Mock
	private JobRepository jobRepository;

	@InjectMocks
	private BaseConfig cut;

	@Test
	@DisplayName("When calling simpleJobLauncher, I expect a JobLauncher to be returned")
	void simpleJobLauncherExpectAJobLauncherToBeReturned() {
		// GIVEN / WHEN
		final var jobLauncher = cut.simpleJobLauncher();

		// THEN
		final var jobRepositoryInJobLauncher = field("jobRepository").ofType(JobRepository.class).in(jobLauncher).get();
		assertThat(jobLauncher).isInstanceOf(SimpleJobLauncher.class);
		assertThat(jobRepositoryInJobLauncher).isEqualTo(jobRepository);
		verifyNoMoreInteractions(jobRepository);
	}
}