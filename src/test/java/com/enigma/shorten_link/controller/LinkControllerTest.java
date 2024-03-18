package com.enigma.shorten_link.controller;

import com.enigma.shorten_link.constant.APIUrl;
import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.entity.Link;
import com.enigma.shorten_link.model.base.CommonResponse;
import com.enigma.shorten_link.model.request.SearchLinkRequest;
import com.enigma.shorten_link.model.request.ShortenLinkRequest;
import com.enigma.shorten_link.model.response.LinkResponse;
import com.enigma.shorten_link.service.LinkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class LinkControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private LinkService service;

    @Test
    void shorten_WhenValid_ShouldReturnLinkResponse() throws Exception {
        // Given
        ShortenLinkRequest request = ShortenLinkRequest.builder()
                .name("string")
                .shortUrl("string")
                .realUrl("string")
                .build();
        String payload = mapper.writeValueAsString(request);
        LinkResponse expected = LinkResponse.builder()
                .id("0c9cfe04-413f-4dc4-a739-c6872c08cd42")
                .created_at(new Date())
                .name(request.getName())
                .shortUrl(request.getShortUrl())
                .realUrl(request.getRealUrl())
                .userId("4ce75680-de39-48be-bb1d-21b3045dbc1e")
                .build();

        // Stubbing
        when(service.shorten(any())).thenReturn(expected);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.post(APIUrl.LINK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                ).andExpect(status().isCreated())
                .andDo(
                        result -> {
                            CommonResponse<LinkResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<LinkResponse>>() {
                            });

                            assertAll(
                                    "Should return Link Response",
                                    () -> assertEquals(expected.getId(), actual.getData().getId()),
                                    () -> assertEquals(expected.getName(), actual.getData().getName()),
                                    () -> assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, actual.getMessage())
                            );
                        }
                ).andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUrl_WhenValid_ShouldHave200ThenReturnLinkResponse() throws Exception {
        // Given
        LinkResponse expected = LinkResponse.builder()
                .id("0c9cfe04-413f-4dc4-a739-c6872c08cd42")
                .created_at(new Date())
                .name("string")
                .shortUrl("string")
                .realUrl("string")
                .userId("4ce75680-de39-48be-bb1d-21b3045dbc1e")
                .build();

        // Stubbing
        when(service.redirect(any())).thenReturn(expected);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.get(APIUrl.LINK + "/{shortUrl}", expected.getShortUrl())
                ).andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<LinkResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertAll(
                            "Should Have 200 and return success message",
                            () -> assertEquals(ResponseMessage.SUCCESS_GET_DATA, actual.getMessage()),
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(expected.getId(), actual.getData().getId())
                    );

                }).andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserUrl_WhenValid_ShouldReturnLinkResponses() throws Exception {
        // Given
        SearchLinkRequest request = SearchLinkRequest.builder()
                .userId("c6a1870e-e83d-425f-8763-e0ed255ee3fa")
                .page(1)
                .size(10)
                .sortBy("name")
                .direction("asc")
                .build();


        List<LinkResponse> list = List.of(
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build(),
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build(),
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build()
        );

        Page<LinkResponse> paging = new Page<LinkResponse>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return list.size();
            }

            @Override
            public <U> Page<U> map(Function<? super LinkResponse, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return request.getPage() - 1;
            }

            @Override
            public int getSize() {
                return request.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return list.size();
            }

            @Override
            public List<LinkResponse> getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return true;
            }

            @Override
            public Sort getSort() {
                return Sort.by(request.getSortBy());
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public boolean isLast() {
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<LinkResponse> iterator() {
                return null;
            }
        };

        // Stubbing
        when(service.findByUserId(any())).thenReturn(paging);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.get(APIUrl.LINK + "/users/" + request.getUserId() + request.getPathVariable())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<List<LinkResponse>> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Get All User",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_GET_DATA, actual.getMessage()),
                            () -> assertEquals(list.size(), actual.getData().size())
                    );
                });
    }

    @Test
    void redirect_WhenValid_ShouldHave200ThenReturnLinkResponse() throws Exception {
        // Given
        LinkResponse expected = LinkResponse.builder()
                .id("0c9cfe04-413f-4dc4-a739-c6872c08cd42")
                .created_at(new Date())
                .name("string")
                .shortUrl("string")
                .realUrl("string")
                .userId("4ce75680-de39-48be-bb1d-21b3045dbc1e")
                .build();

        // Stubbing
        when(service.redirect(any())).thenReturn(expected);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.get( "/{shortUrl}", expected.getShortUrl())
                ).andExpect(
                        header().stringValues("Location", expected.getRealUrl())
                )
                .andExpect(status().isTemporaryRedirect())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update_WhenValid_ShouldReturnLinkResponse() throws Exception {
        // Given
        ShortenLinkRequest request = ShortenLinkRequest.builder()
                .name("string")
                .shortUrl("string")
                .realUrl("string")
                .build();
        String payload = mapper.writeValueAsString(request);
        LinkResponse expected = LinkResponse.builder()
                .id("0c9cfe04-413f-4dc4-a739-c6872c08cd42")
                .created_at(new Date())
                .name(request.getName())
                .shortUrl(request.getShortUrl())
                .realUrl(request.getRealUrl())
                .userId("4ce75680-de39-48be-bb1d-21b3045dbc1e")
                .build();

        // Stubbing
        when(service.update(any())).thenReturn(expected);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.put(APIUrl.LINK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                ).andExpect(status().isOk())
                .andDo(
                        result -> {
                            CommonResponse<LinkResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<LinkResponse>>() {
                            });

                            assertAll(
                                    "Should return Link Response",
                                    () -> assertEquals(expected.getId(), actual.getData().getId()),
                                    () -> assertEquals(expected.getName(), actual.getData().getName()),
                                    () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                                    () -> assertEquals(ResponseMessage.SUCCESS_UPDATE_DATA, actual.getMessage())
                            );
                        }
                ).andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_WhenValid_ShouldHave200() throws Exception {
        // Given
        String id = UUID.randomUUID().toString();

        // Stubbing
        doNothing().when(service).deleteByid(any());

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(APIUrl.LINK + "/{id}/delete", id)
                ).andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<String> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<String>>() {
                    });

                    assertAll(
                            "Should Have 200 and return success message",
                            () -> assertEquals(ResponseMessage.SUCCESS_DELETE_DATA, actual.getMessage()),
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode())
                    );

                }).andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAll_WhenValid_ShouldReturnLinkResponses() throws Exception {
        // Given
        SearchLinkRequest request = SearchLinkRequest.builder()
                .userId("c6a1870e-e83d-425f-8763-e0ed255ee3fa")
                .page(1)
                .size(10)
                .sortBy("name")
                .direction("asc")
                .build();


        List<LinkResponse> list = List.of(
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build(),
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build(),
                LinkResponse.builder()
                        .created_at(new Date())
                        .userId(request.getUserId())
                        .build()
        );

        Page<LinkResponse> paging = new Page<LinkResponse>() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return list.size();
            }

            @Override
            public <U> Page<U> map(Function<? super LinkResponse, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return request.getPage() - 1;
            }

            @Override
            public int getSize() {
                return request.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return list.size();
            }

            @Override
            public List<LinkResponse> getContent() {
                return list;
            }

            @Override
            public boolean hasContent() {
                return true;
            }

            @Override
            public Sort getSort() {
                return Sort.by(request.getSortBy());
            }

            @Override
            public boolean isFirst() {
                return true;
            }

            @Override
            public boolean isLast() {
                return true;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<LinkResponse> iterator() {
                return null;
            }
        };

        // Stubbing
        when(service.findAll(any())).thenReturn(paging);

        // When - Then
        mockMvc.perform(
                        MockMvcRequestBuilders.get(APIUrl.LINK )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<List<LinkResponse>> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertAll(
                            "Get All User",
                            () -> assertEquals(HttpStatus.OK.value(), actual.getStatusCode()),
                            () -> assertEquals(ResponseMessage.SUCCESS_GET_DATA, actual.getMessage()),
                            () -> assertEquals(list.size(), actual.getData().size())
                    );
                });
    }
}