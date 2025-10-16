package ink.kindler.metasearch.integration.runner;

import ink.kindler.metasearch.integration.service.GutenbergAustraliaService;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GutenbergAustraliaRunnerTest {

  @InjectMocks
  private GutenbergAustraliaRunner gutenbergAustraliaRunner;

  @Mock
  private GutenbergAustraliaService gutenbergAustraliaService;

  @Mock
  private LockProvider lockProvider;

  @Test
  void shouldSkipIndexing() throws Exception {
    when(gutenbergAustraliaService.shouldSkipIndexing()).thenReturn(true);

    gutenbergAustraliaRunner.run();

    verifyNoInteractions(lockProvider);
  }

  @Test
  void shouldNotRunIndexingWhenAnotherInstanceIndexingIsInProgress() throws Exception {
    var lock = mock(SimpleLock.class);
    when(gutenbergAustraliaService.shouldSkipIndexing()).thenReturn(false);
    when(lockProvider.lock(any(LockConfiguration.class))).thenReturn(Optional.empty());

    gutenbergAustraliaRunner.run();

    verify(gutenbergAustraliaService, never()).refreshIndex(any());
    verify(lock, never()).unlock();
  }

  @Test
  void shouldIndex() throws Exception {
    var lock = mock(SimpleLock.class);
    when(gutenbergAustraliaService.shouldSkipIndexing()).thenReturn(false);
    when(lockProvider.lock(any(LockConfiguration.class))).thenReturn(Optional.of(lock));

    gutenbergAustraliaRunner.run();

    verify(gutenbergAustraliaService).refreshIndex(any());
    verify(lock).unlock();
  }
}